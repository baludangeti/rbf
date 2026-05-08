package com.rbf.product.billing.service;

import com.rbf.product.billing.client.ProductResponse;
import com.rbf.product.billing.client.InternationalTaxCalculationRequest;
import com.rbf.product.billing.client.InternationalTaxCalculationResponse;
import com.rbf.product.billing.client.SaveInvoiceTaxBreakupRequest;
import com.rbf.product.billing.client.ServiceClient;
import com.rbf.product.billing.dto.CreateInvoiceItemRequest;
import com.rbf.product.billing.dto.CreateInvoiceRequest;
import com.rbf.product.billing.dto.FinalizeInvoiceRequest;
import com.rbf.product.billing.dto.HoldInvoiceRequest;
import com.rbf.product.billing.dto.InvoiceItemResponse;
import com.rbf.product.billing.dto.InvoiceResponse;
import com.rbf.product.billing.model.Invoice;
import com.rbf.product.billing.model.InvoiceItem;
import com.rbf.product.billing.model.InvoiceStatus;
import com.rbf.product.billing.repository.InvoiceRepository;
import com.rbf.product.common.tenant.OrgContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final InvoiceRepository invoiceRepository;
    private final ServiceClient serviceClient;

    public BillingService(InvoiceRepository invoiceRepository, ServiceClient serviceClient) {
        this.invoiceRepository = invoiceRepository;
        this.serviceClient = serviceClient;
    }

    public List<InvoiceResponse> list() {
        return invoiceRepository.findByOrgIdOrderByCreatedAtDesc(OrgContext.requireOrgId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<InvoiceResponse> listPage(Pageable pageable) {
        return invoiceRepository.findByOrgId(OrgContext.requireOrgId(), pageable)
                .map(this::toResponse);
    }

    public Page<InvoiceResponse> listByStatusPage(InvoiceStatus status, Pageable pageable) {
        return invoiceRepository.findByOrgIdAndStatus(OrgContext.requireOrgId(), status, pageable)
                .map(this::toResponse);
    }

    public List<InvoiceResponse> listDrafts() {
        return invoiceRepository.findByOrgIdAndStatusOrderByUpdatedAtDesc(OrgContext.requireOrgId(), InvoiceStatus.DRAFT)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InvoiceResponse> listHeld() {
        return invoiceRepository.findByOrgIdAndStatusOrderByUpdatedAtDesc(OrgContext.requireOrgId(), InvoiceStatus.HELD)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InvoiceResponse get(Long id) {
        Invoice invoice = invoiceRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        return toResponse(invoice);
    }

    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = buildInvoice(request, InvoiceStatus.FINALIZED);
        finalizeInvoice(invoice, request.getPaymentAmount(), request.getCustomerId(), request.isCreditSale());
        return toResponse(invoice);
    }

    @Transactional
    public InvoiceResponse createDraft(CreateInvoiceRequest request) {
        Invoice invoice = buildInvoice(request, InvoiceStatus.DRAFT);
        Invoice saved = invoiceRepository.save(invoice);
        log.info("Draft invoice saved orgId={} invoiceId={} total={}", saved.getOrgId(), saved.getId(), saved.getTotal());
        return toResponse(saved);
    }

    @Transactional
    public InvoiceResponse updateDraftCart(Long invoiceId, CreateInvoiceRequest request) {
        Invoice invoice = getMutableInvoice(invoiceId);
        invoice.clearItems();
        applyTaxContext(invoice, request);
        applyCart(invoice, request);
        Invoice saved = invoiceRepository.save(invoice);
        log.info("Draft invoice cart updated orgId={} invoiceId={} itemCount={}",
                saved.getOrgId(), saved.getId(), saved.getItems().size());
        return toResponse(saved);
    }

    @Transactional
    public InvoiceResponse holdInvoice(Long invoiceId, HoldInvoiceRequest request) {
        Invoice invoice = getMutableInvoice(invoiceId);
        invoice.setStatus(InvoiceStatus.HELD);
        invoice.setHoldReference(request.getHoldReference());
        Invoice saved = invoiceRepository.save(invoice);
        log.info("Invoice held orgId={} invoiceId={} holdReference={}",
                saved.getOrgId(), saved.getId(), saved.getHoldReference());
        return toResponse(saved);
    }

    @Transactional
    public InvoiceResponse resumeInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findByIdAndOrgId(invoiceId, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        if (invoice.getStatus() != InvoiceStatus.HELD) {
            throw new IllegalArgumentException("Only held invoices can be resumed");
        }
        invoice.setStatus(InvoiceStatus.DRAFT);
        Invoice saved = invoiceRepository.save(invoice);
        log.info("Invoice resumed orgId={} invoiceId={}", saved.getOrgId(), saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public InvoiceResponse resumeInvoiceByReference(String holdReference) {
        Invoice invoice = invoiceRepository.findByHoldReferenceAndOrgId(holdReference, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Held invoice not found"));
        return resumeInvoice(invoice.getId());
    }

    @Transactional
    public InvoiceResponse finalizeDraft(Long invoiceId, FinalizeInvoiceRequest request) {
        Invoice invoice = getMutableInvoice(invoiceId);
        invoice.setStatus(InvoiceStatus.FINALIZED);
        finalizeInvoice(invoice, request.getPaymentAmount(), request.getCustomerId(), request.isCreditSale());
        return toResponse(invoice);
    }

    private Invoice buildInvoice(CreateInvoiceRequest request, InvoiceStatus status) {
        Long orgId = OrgContext.requireOrgId();
        log.info("Billing transaction started orgId={} itemCount={} discount={} paymentAmount={}",
                orgId, request.getItems().size(), request.getDiscount(), request.getPaymentAmount());

        Invoice invoice = new Invoice();
        invoice.setOrgId(orgId);
        invoice.setStatus(status);
        invoice.setHoldReference(null);
        applyTaxContext(invoice, request);
        applyCart(invoice, request);
        return invoiceRepository.save(invoice);
    }

    private void applyTaxContext(Invoice invoice, CreateInvoiceRequest request) {
        invoice.setSellerCountry(request.getSellerCountry());
        invoice.setSellerRegion(request.getSellerRegion());
        invoice.setCustomerCountry(request.getCustomerCountry());
        invoice.setCustomerRegion(request.getCustomerRegion());
        invoice.setCustomerType(request.getCustomerType());
        invoice.setCustomerTaxExempt(request.isCustomerTaxExempt());
        invoice.setReverseChargeApplicable(request.isReverseChargeApplicable());
        invoice.setCurrencyCode(request.getCurrencyCode());
        invoice.setExchangeRate(request.getExchangeRate());
        invoice.setTransactionType(request.getTransactionType());
    }

    private void applyCart(Invoice invoice, CreateInvoiceRequest request) {
        Long orgId = OrgContext.requireOrgId();
        invoice.setDiscount(request.getDiscount() == null ? BigDecimal.ZERO : request.getDiscount());
        invoice.setDiscountPercentage(request.getDiscountPercentage() == null ? BigDecimal.ZERO : request.getDiscountPercentage());

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;

        for (CreateInvoiceItemRequest itemRequest : request.getItems()) {
            log.info("Fetching product for billing orgId={} productId={} qty={}",
                    orgId, itemRequest.getProductId(), itemRequest.getQty());
            ProductResponse product = resolveProduct(itemRequest);
            if (product == null) {
                log.warn("Billing product lookup failed orgId={} productId={}", orgId, itemRequest.getProductId());
                throw new IllegalArgumentException("Product not found: " + itemRequest.getProductId());
            }

            BigDecimal qty = BigDecimal.valueOf(itemRequest.getQty());
            BigDecimal lineAmount = product.getPrice().multiply(qty);
            BigDecimal lineDiscount = lineAmount
                    .multiply(invoice.getDiscountPercentage())
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
            InternationalTaxCalculationResponse taxResponse = serviceClient.calculateTax(
                    taxRequest(invoice, itemRequest, product, lineDiscount)
            );
            BigDecimal lineTax = taxResponse.getTotalTaxAmount();
            BigDecimal taxableAmount = taxResponse.getTaxableAmount();
            BigDecimal taxRate = taxableAmount.signum() == 0
                    ? BigDecimal.ZERO
                    : lineTax.multiply(ONE_HUNDRED).divide(taxableAmount, 2, RoundingMode.HALF_UP);

            InvoiceItem item = new InvoiceItem();
            item.setOrgId(orgId);
            item.setProductId(product.getId());
            item.setQty(itemRequest.getQty());
            item.setPrice(product.getPrice());
            item.setTax(lineTax);
            item.setTaxableAmount(taxableAmount);
            item.setTaxRate(taxRate);
            item.setProductCategoryId(itemRequest.getProductCategoryId());
            item.setHsnSacCode(itemRequest.getHsnSacCode());
            item.setLineTotal(taxResponse.getTotalAmount());
            invoice.addItem(item);

            subtotal = subtotal.add(lineAmount);
            totalTax = totalTax.add(lineTax);
        }

        BigDecimal percentageDiscount = subtotal
                .multiply(invoice.getDiscountPercentage())
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
        BigDecimal totalDiscount = invoice.getDiscount().add(percentageDiscount);
        BigDecimal rawTotal = subtotal.add(totalTax).subtract(totalDiscount);
        if (rawTotal.signum() < 0) {
            log.warn("Billing discount exceeds amount orgId={} subtotal={} tax={} discount={}",
                    orgId, subtotal, totalTax, totalDiscount);
            throw new IllegalArgumentException("Discount cannot exceed invoice amount");
        }

        BigDecimal roundedTotal = rawTotal.setScale(0, RoundingMode.HALF_UP).setScale(2, RoundingMode.UNNECESSARY);
        BigDecimal roundOff = roundedTotal.subtract(rawTotal).setScale(2, RoundingMode.HALF_UP);
        invoice.setSubtotal(subtotal);
        invoice.setTax(totalTax);
        invoice.setDiscount(totalDiscount);
        invoice.setRoundOff(roundOff);
        invoice.setTotal(roundedTotal);
    }

    private void finalizeInvoice(Invoice invoice, BigDecimal paymentAmount, Long customerId, boolean creditSale) {
        Long orgId = OrgContext.requireOrgId();
        BigDecimal paidAmount = paymentAmount == null ? BigDecimal.ZERO : paymentAmount;
        BigDecimal creditAmount = invoice.getTotal().subtract(paidAmount);
        if (creditAmount.signum() < 0) {
            throw new IllegalArgumentException("Payment cannot exceed invoice total");
        }
        if (creditSale) {
            if (customerId == null) {
                throw new IllegalArgumentException("Customer is required for credit sale");
            }
            if (creditAmount.signum() <= 0) {
                throw new IllegalArgumentException("Credit sale requires due amount");
            }
            var validation = serviceClient.validateCreditLimit(customerId, creditAmount);
            if (validation == null || !validation.isAllowed()) {
                throw new IllegalArgumentException("Customer credit limit exceeded");
            }
        }

        for (InvoiceItem item : invoice.getItems()) {
            log.info("Deducting stock for billing orgId={} productId={} qty={}",
                    orgId, item.getProductId(), item.getQty());
            serviceClient.deductStock(item.getProductId(), item.getQty());
        }
        invoice.setStatus(InvoiceStatus.FINALIZED);
        Invoice saved = invoiceRepository.save(invoice);
        saveInvoiceTaxBreakups(saved);
        log.info("Invoice saved orgId={} invoiceId={} total={} tax={} discount={}",
                orgId, saved.getId(), saved.getTotal(), saved.getTax(), saved.getDiscount());

        serviceClient.savePayment(saved.getId(), paidAmount, saved.getTotal());
        log.info("Payment save requested orgId={} invoiceId={} amount={}",
                orgId, saved.getId(), paidAmount);

        if (creditSale) {
            serviceClient.recordCreditSale(customerId, saved.getId(), creditAmount);
            log.info("Credit sale posted orgId={} invoiceId={} customerId={} dueAmount={}",
                    orgId, saved.getId(), customerId, creditAmount);
        }

        serviceClient.postLedgerEntry(saved.getId(), saved.getTotal(), saved.getTax(), saved.getDiscount());
        log.info("Ledger entry requested orgId={} invoiceId={} total={}",
                orgId, saved.getId(), saved.getTotal());
        log.info("Billing transaction completed orgId={} invoiceId={}", orgId, saved.getId());
    }

    private void saveInvoiceTaxBreakups(Invoice invoice) {
        for (InvoiceItem item : invoice.getItems()) {
            InternationalTaxCalculationResponse tax = serviceClient.calculateTax(taxRequest(invoice, item));
            serviceClient.saveInvoiceTaxBreakup(new SaveInvoiceTaxBreakupRequest(
                    invoice.getId(),
                    item.getId(),
                    invoice.getCustomerCountry(),
                    invoice.getCustomerRegion(),
                    tax.getTaxBreakups()
            ));
        }
    }

    private Invoice getMutableInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findByIdAndOrgId(invoiceId, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        if (invoice.getStatus() == InvoiceStatus.FINALIZED || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalArgumentException("Finalized or cancelled invoices cannot be modified");
        }
        return invoice;
    }

    private ProductResponse resolveProduct(CreateInvoiceItemRequest itemRequest) {
        if (itemRequest.getProductId() != null) {
            return serviceClient.getProduct(itemRequest.getProductId());
        }
        if (itemRequest.getBarcode() != null && !itemRequest.getBarcode().isBlank()) {
            return serviceClient.getProductByBarcode(itemRequest.getBarcode());
        }
        if (itemRequest.getSku() != null && !itemRequest.getSku().isBlank()) {
            return serviceClient.getProductBySku(itemRequest.getSku());
        }
        throw new IllegalArgumentException("Product id, SKU, or barcode is required");
    }

    private InternationalTaxCalculationRequest taxRequest(Invoice invoice, CreateInvoiceItemRequest itemRequest,
                                                         ProductResponse product, BigDecimal discountAmount) {
        InternationalTaxCalculationRequest request = new InternationalTaxCalculationRequest();
        request.setOrgId(invoice.getOrgId());
        request.setSellerCountry(invoice.getSellerCountry());
        request.setSellerRegion(invoice.getSellerRegion());
        request.setCustomerCountry(invoice.getCustomerCountry());
        request.setCustomerRegion(invoice.getCustomerRegion());
        request.setCustomerType(invoice.getCustomerType());
        request.setCustomerTaxExempt(invoice.isCustomerTaxExempt());
        request.setReverseChargeApplicable(invoice.isReverseChargeApplicable());
        request.setProductId(product.getId());
        request.setProductCategoryId(itemRequest.getProductCategoryId());
        request.setHsnSacCode(itemRequest.getHsnSacCode());
        request.setQuantity(BigDecimal.valueOf(itemRequest.getQty()));
        request.setUnitPrice(product.getPrice());
        request.setDiscountAmount(discountAmount);
        request.setCurrencyCode(invoice.getCurrencyCode());
        request.setExchangeRate(invoice.getExchangeRate());
        request.setTransactionType(invoice.getTransactionType());
        return request;
    }

    private InternationalTaxCalculationRequest taxRequest(Invoice invoice, InvoiceItem item) {
        InternationalTaxCalculationRequest request = new InternationalTaxCalculationRequest();
        request.setOrgId(invoice.getOrgId());
        request.setSellerCountry(invoice.getSellerCountry());
        request.setSellerRegion(invoice.getSellerRegion());
        request.setCustomerCountry(invoice.getCustomerCountry());
        request.setCustomerRegion(invoice.getCustomerRegion());
        request.setCustomerType(invoice.getCustomerType());
        request.setCustomerTaxExempt(invoice.isCustomerTaxExempt());
        request.setReverseChargeApplicable(invoice.isReverseChargeApplicable());
        request.setProductId(item.getProductId());
        request.setProductCategoryId(item.getProductCategoryId());
        request.setHsnSacCode(item.getHsnSacCode());
        request.setQuantity(BigDecimal.valueOf(item.getQty()));
        request.setUnitPrice(item.getPrice());
        BigDecimal lineAmount = item.getPrice().multiply(BigDecimal.valueOf(item.getQty()));
        request.setDiscountAmount(lineAmount.subtract(item.getTaxableAmount()));
        request.setCurrencyCode(invoice.getCurrencyCode());
        request.setExchangeRate(invoice.getExchangeRate());
        request.setTransactionType(invoice.getTransactionType());
        return request;
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        List<InvoiceItemResponse> items = invoice.getItems().stream()
                .map(item -> new InvoiceItemResponse(item.getProductId(), item.getQty(), item.getPrice(),
                        item.getTax(), item.getTaxRate(), item.getLineTotal()))
                .toList();
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getOrgId(),
                invoice.getTotal(),
                invoice.getSubtotal(),
                invoice.getTax(),
                invoice.getDiscount(),
                invoice.getDiscountPercentage(),
                invoice.getRoundOff(),
                invoice.getStatus(),
                invoice.getHoldReference(),
                invoice.getCreatedAt(),
                items
        );
    }
}
