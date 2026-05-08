package com.rbf.product.salesreturn.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.salesreturn.client.InvoiceItemResponse;
import com.rbf.product.salesreturn.client.InvoiceResponse;
import com.rbf.product.salesreturn.client.SalesReturnClient;
import com.rbf.product.salesreturn.dto.CreateSalesReturnItemRequest;
import com.rbf.product.salesreturn.dto.CreateSalesReturnRequest;
import com.rbf.product.salesreturn.dto.SalesReturnItemResponse;
import com.rbf.product.salesreturn.dto.SalesReturnResponse;
import com.rbf.product.salesreturn.model.RefundStatus;
import com.rbf.product.salesreturn.model.SalesReturn;
import com.rbf.product.salesreturn.model.SalesReturnItem;
import com.rbf.product.salesreturn.repository.SalesReturnItemRepository;
import com.rbf.product.salesreturn.repository.SalesReturnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesReturnService {

    private static final Logger log = LoggerFactory.getLogger(SalesReturnService.class);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final SalesReturnRepository salesReturnRepository;
    private final SalesReturnItemRepository salesReturnItemRepository;
    private final SalesReturnClient salesReturnClient;

    public SalesReturnService(SalesReturnRepository salesReturnRepository,
                              SalesReturnItemRepository salesReturnItemRepository,
                              SalesReturnClient salesReturnClient) {
        this.salesReturnRepository = salesReturnRepository;
        this.salesReturnItemRepository = salesReturnItemRepository;
        this.salesReturnClient = salesReturnClient;
    }

    public List<SalesReturnResponse> list() {
        return salesReturnRepository.findByOrgIdOrderByCreatedAtDesc(OrgContext.requireOrgId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SalesReturnResponse> listByInvoice(Long invoiceId) {
        return salesReturnRepository.findByInvoiceIdAndOrgIdOrderByCreatedAtAsc(invoiceId, OrgContext.requireOrgId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public SalesReturnResponse get(Long id) {
        return toResponse(getSalesReturn(id));
    }

    @Transactional
    public SalesReturnResponse createReturn(CreateSalesReturnRequest request) {
        Long orgId = OrgContext.requireOrgId();
        salesReturnRepository.findByReturnNoAndOrgId(request.getReturnNo(), orgId)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Sales return number already exists");
                });

        InvoiceResponse invoice = salesReturnClient.getInvoice(request.getInvoiceId());
        if (invoice == null || !"FINALIZED".equals(invoice.getStatus())) {
            throw new IllegalArgumentException("Only finalized invoices can be returned");
        }

        Map<Long, InvoiceItemResponse> invoiceItems = invoice.getItems().stream()
                .collect(Collectors.toMap(InvoiceItemResponse::getProductId, Function.identity(), this::mergeInvoiceItems));
        Map<Long, Integer> alreadyReturned = salesReturnItemRepository.findByInvoiceIdAndOrgId(request.getInvoiceId(), orgId)
                .stream()
                .collect(Collectors.groupingBy(SalesReturnItem::getProductId, Collectors.summingInt(SalesReturnItem::getReturnQty)));

        SalesReturn salesReturn = new SalesReturn();
        salesReturn.setOrgId(orgId);
        salesReturn.setInvoiceId(request.getInvoiceId());
        salesReturn.setReturnNo(request.getReturnNo());
        salesReturn.setReturnDate(request.getReturnDate());
        salesReturn.setReason(request.getReason());

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;

        for (CreateSalesReturnItemRequest itemRequest : request.getItems()) {
            InvoiceItemResponse invoiceItem = invoiceItems.get(itemRequest.getProductId());
            if (invoiceItem == null) {
                throw new IllegalArgumentException("Product is not part of invoice: " + itemRequest.getProductId());
            }
            int previousReturnQty = alreadyReturned.getOrDefault(itemRequest.getProductId(), 0);
            if (previousReturnQty + itemRequest.getReturnQty() > invoiceItem.getQty()) {
                throw new IllegalArgumentException("Return quantity exceeds invoice quantity for product " + itemRequest.getProductId());
            }

            BigDecimal returnQty = BigDecimal.valueOf(itemRequest.getReturnQty());
            BigDecimal lineAmount = invoiceItem.getPrice().multiply(returnQty);
            BigDecimal lineTax = lineAmount.multiply(invoiceItem.getTaxRate())
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);

            SalesReturnItem returnItem = new SalesReturnItem();
            returnItem.setOrgId(orgId);
            returnItem.setInvoiceId(request.getInvoiceId());
            returnItem.setProductId(itemRequest.getProductId());
            returnItem.setReturnQty(itemRequest.getReturnQty());
            returnItem.setPrice(invoiceItem.getPrice());
            returnItem.setTaxRate(invoiceItem.getTaxRate());
            returnItem.setTax(lineTax);
            returnItem.setLineTotal(lineAmount.add(lineTax));
            salesReturn.addItem(returnItem);

            subtotal = subtotal.add(lineAmount);
            tax = tax.add(lineTax);
        }

        BigDecimal refundAmount = subtotal.add(tax);
        salesReturn.setSubtotal(subtotal);
        salesReturn.setTax(tax);
        salesReturn.setRefundAmount(refundAmount);
        salesReturn.setRefundStatus(request.isRefundNow() ? RefundStatus.REFUNDED : RefundStatus.PENDING);

        SalesReturn saved = salesReturnRepository.save(salesReturn);
        restoreInventory(saved);
        if (request.isRefundNow()) {
            salesReturnClient.recordRefund(saved.getInvoiceId(), saved.getRefundAmount(), saved.getReturnNo());
        }
        salesReturnClient.postReversal(saved.getInvoiceId(), saved.getRefundAmount(), saved.getTax(), saved.getReturnNo());

        log.info("Sales return created orgId={} returnId={} invoiceId={} refundAmount={} refundStatus={}",
                orgId, saved.getId(), saved.getInvoiceId(), saved.getRefundAmount(), saved.getRefundStatus());
        return toResponse(saved);
    }

    private void restoreInventory(SalesReturn salesReturn) {
        Long orgId = OrgContext.requireOrgId();
        for (SalesReturnItem item : salesReturn.getItems()) {
            salesReturnClient.restoreStock(item.getProductId(), item.getReturnQty());
            log.info("Inventory restored from sales return orgId={} returnId={} productId={} qty={}",
                    orgId, salesReturn.getId(), item.getProductId(), item.getReturnQty());
        }
    }

    private InvoiceItemResponse mergeInvoiceItems(InvoiceItemResponse first, InvoiceItemResponse second) {
        first.setQty(first.getQty() + second.getQty());
        first.setTax(first.getTax().add(second.getTax()));
        first.setLineTotal(first.getLineTotal().add(second.getLineTotal()));
        return first;
    }

    private SalesReturn getSalesReturn(Long id) {
        return salesReturnRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Sales return not found"));
    }

    private SalesReturnResponse toResponse(SalesReturn salesReturn) {
        List<SalesReturnItemResponse> items = salesReturn.getItems().stream()
                .map(item -> new SalesReturnItemResponse(
                        item.getProductId(),
                        item.getReturnQty(),
                        item.getPrice(),
                        item.getTaxRate(),
                        item.getTax(),
                        item.getLineTotal()
                ))
                .toList();

        return new SalesReturnResponse(
                salesReturn.getId(),
                salesReturn.getOrgId(),
                salesReturn.getInvoiceId(),
                salesReturn.getReturnNo(),
                salesReturn.getReturnDate(),
                salesReturn.getReason(),
                salesReturn.getSubtotal(),
                salesReturn.getTax(),
                salesReturn.getRefundAmount(),
                salesReturn.getRefundStatus(),
                items
        );
    }
}
