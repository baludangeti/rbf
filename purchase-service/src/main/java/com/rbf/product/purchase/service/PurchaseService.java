package com.rbf.product.purchase.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.purchase.client.InventoryClient;
import com.rbf.product.purchase.dto.CreatePurchaseItemRequest;
import com.rbf.product.purchase.dto.CreatePurchaseRequest;
import com.rbf.product.purchase.dto.GrnRequest;
import com.rbf.product.purchase.dto.PurchaseItemResponse;
import com.rbf.product.purchase.dto.PurchaseReturnRequest;
import com.rbf.product.purchase.dto.PurchaseReturnResponse;
import com.rbf.product.purchase.dto.PurchaseResponse;
import com.rbf.product.purchase.model.Purchase;
import com.rbf.product.purchase.model.PurchaseItem;
import com.rbf.product.purchase.model.PurchaseStatus;
import com.rbf.product.purchase.repository.PurchaseRepository;
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
public class PurchaseService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseService.class);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final PurchaseRepository purchaseRepository;
    private final InventoryClient inventoryClient;

    public PurchaseService(PurchaseRepository purchaseRepository, InventoryClient inventoryClient) {
        this.purchaseRepository = purchaseRepository;
        this.inventoryClient = inventoryClient;
    }

    public List<PurchaseResponse> list() {
        return purchaseRepository.findByOrgIdOrderByCreatedAtDesc(OrgContext.requireOrgId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public PurchaseResponse get(Long id) {
        return toResponse(getPurchase(id));
    }

    @Transactional
    public PurchaseResponse createPurchaseOrder(CreatePurchaseRequest request) {
        Long orgId = OrgContext.requireOrgId();
        purchaseRepository.findByPurchaseOrderNoAndOrgId(request.getPurchaseOrderNo(), orgId)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Purchase order number already exists");
                });

        Purchase purchase = new Purchase();
        purchase.setOrgId(orgId);
        purchase.setSupplierId(request.getSupplierId());
        purchase.setSupplierName(request.getSupplierName());
        purchase.setPurchaseOrderNo(request.getPurchaseOrderNo());
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setStatus(PurchaseStatus.ORDERED);

        applyItems(purchase, request.getItems());
        Purchase saved = purchaseRepository.save(purchase);
        log.info("Purchase order created orgId={} purchaseId={} poNo={} total={}",
                orgId, saved.getId(), saved.getPurchaseOrderNo(), saved.getTotal());
        return toResponse(saved);
    }

    @Transactional
    public PurchaseResponse receiveGoods(Long purchaseId, GrnRequest request) {
        Long orgId = OrgContext.requireOrgId();
        Purchase purchase = getPurchase(purchaseId);
        if (purchase.getStatus() == PurchaseStatus.RECEIVED || purchase.getStatus() == PurchaseStatus.CANCELLED) {
            throw new IllegalArgumentException("Purchase cannot be received in current status");
        }

        Map<Long, GrnRequest.GrnItemRequest> receivedItems = request.getItems().stream()
                .collect(Collectors.toMap(GrnRequest.GrnItemRequest::getProductId, Function.identity()));

        for (PurchaseItem item : purchase.getItems()) {
            GrnRequest.GrnItemRequest received = receivedItems.get(item.getProductId());
            int receivedQuantity = received == null ? 0 : received.getReceivedQuantity();
            if (receivedQuantity > item.getQuantity()) {
                throw new IllegalArgumentException("Received quantity exceeds ordered quantity for product " + item.getProductId());
            }
            item.setReceivedQuantity(receivedQuantity);
            if (receivedQuantity > 0) {
                inventoryClient.addStock(item.getProductId(), receivedQuantity);
                log.info("Inventory increased from GRN orgId={} purchaseId={} productId={} quantity={}",
                        orgId, purchase.getId(), item.getProductId(), receivedQuantity);
            }
        }

        purchase.setGrnNo(request.getGrnNo());
        purchase.setStatus(PurchaseStatus.RECEIVED);
        Purchase saved = purchaseRepository.save(purchase);
        log.info("GRN completed orgId={} purchaseId={} grnNo={}", orgId, saved.getId(), saved.getGrnNo());
        return toResponse(saved);
    }

    @Transactional
    public PurchaseReturnResponse returnPurchase(Long purchaseId, PurchaseReturnRequest request) {
        Purchase purchase = getPurchase(purchaseId);
        if (purchase.getStatus() != PurchaseStatus.RECEIVED) {
            throw new IllegalArgumentException("Only received purchases can be returned");
        }

        Map<Long, PurchaseItem> itemByProduct = purchase.getItems().stream()
                .collect(Collectors.toMap(PurchaseItem::getProductId, Function.identity()));
        BigDecimal returnAmount = BigDecimal.ZERO;

        for (PurchaseReturnRequest.PurchaseReturnItemRequest returnItem : request.getItems()) {
            PurchaseItem purchaseItem = itemByProduct.get(returnItem.getProductId());
            if (purchaseItem == null) {
                throw new IllegalArgumentException("Product not found in purchase " + returnItem.getProductId());
            }
            if (returnItem.getReturnQuantity() > purchaseItem.getReceivedQuantity()) {
                throw new IllegalArgumentException("Return quantity exceeds received quantity for product " + returnItem.getProductId());
            }
            BigDecimal quantity = BigDecimal.valueOf(returnItem.getReturnQuantity());
            BigDecimal lineAmount = purchaseItem.getPurchasePrice().multiply(quantity);
            BigDecimal lineTax = lineAmount.multiply(purchaseItem.getTaxRate())
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
            returnAmount = returnAmount.add(lineAmount).add(lineTax);
            inventoryClient.deductStock(returnItem.getProductId(), returnItem.getReturnQuantity());
            log.info("Inventory deducted from purchase return orgId={} purchaseId={} productId={} quantity={}",
                    OrgContext.requireOrgId(), purchase.getId(), returnItem.getProductId(), returnItem.getReturnQuantity());
        }
        return new PurchaseReturnResponse(purchaseId, request.getReturnNo(), returnAmount, request.getReason());
    }

    private void applyItems(Purchase purchase, List<CreatePurchaseItemRequest> itemRequests) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;

        for (CreatePurchaseItemRequest request : itemRequests) {
            BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());
            BigDecimal lineAmount = request.getPurchasePrice().multiply(quantity);
            BigDecimal lineTax = lineAmount.multiply(request.getTaxRate())
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);

            PurchaseItem item = new PurchaseItem();
            item.setOrgId(purchase.getOrgId());
            item.setProductId(request.getProductId());
            item.setQuantity(request.getQuantity());
            item.setReceivedQuantity(0);
            item.setPurchasePrice(request.getPurchasePrice());
            item.setTaxRate(request.getTaxRate());
            item.setTax(lineTax);
            item.setLineTotal(lineAmount.add(lineTax));
            purchase.addItem(item);

            subtotal = subtotal.add(lineAmount);
            totalTax = totalTax.add(lineTax);
        }

        purchase.setSubtotal(subtotal);
        purchase.setTax(totalTax);
        purchase.setTotal(subtotal.add(totalTax));
    }

    private Purchase getPurchase(Long id) {
        return purchaseRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found"));
    }

    private PurchaseResponse toResponse(Purchase purchase) {
        List<PurchaseItemResponse> items = purchase.getItems().stream()
                .map(item -> new PurchaseItemResponse(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getReceivedQuantity(),
                        item.getPurchasePrice(),
                        item.getTaxRate(),
                        item.getTax(),
                        item.getLineTotal()
                ))
                .toList();

        return new PurchaseResponse(
                purchase.getId(),
                purchase.getOrgId(),
                purchase.getSupplierId(),
                purchase.getSupplierName(),
                purchase.getPurchaseOrderNo(),
                purchase.getGrnNo(),
                purchase.getPurchaseDate(),
                purchase.getSubtotal(),
                purchase.getTax(),
                purchase.getTotal(),
                purchase.getStatus(),
                items
        );
    }
}
