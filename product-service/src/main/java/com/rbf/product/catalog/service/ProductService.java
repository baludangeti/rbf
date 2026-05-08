package com.rbf.product.catalog.service;

import com.rbf.product.catalog.model.Product;
import com.rbf.product.catalog.repository.ProductRepository;
import com.rbf.product.common.tenant.OrgContextResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrgContextResolver orgContextResolver;

    public ProductService(ProductRepository productRepository, OrgContextResolver orgContextResolver) {
        this.productRepository = productRepository;
        this.orgContextResolver = orgContextResolver;
    }

    public List<Product> list() {
        Long orgId = orgContextResolver.currentOrgId();
        return productRepository.findByOrgIdOrderByNameAsc(orgId);
    }

    public Page<Product> listPage(String search, Pageable pageable) {
        Long orgId = orgContextResolver.currentOrgId();
        String resolvedSearch = search == null || search.isBlank() ? null : search.trim();
        return productRepository.searchByOrgId(orgId, resolvedSearch, pageable);
    }

    public Product get(Long id) {
        Long orgId = orgContextResolver.currentOrgId();
        return productRepository.findByIdAndOrgId(id, orgId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public Product getBySku(String sku) {
        Long orgId = orgContextResolver.currentOrgId();
        return productRepository.findBySkuAndOrgId(sku, orgId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found for SKU"));
    }

    public Product getByBarcode(String barcode) {
        Long orgId = orgContextResolver.currentOrgId();
        return productRepository.findByBarcodeAndOrgId(barcode, orgId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found for barcode"));
    }

    public List<Product> searchBySku(String sku) {
        Long orgId = orgContextResolver.currentOrgId();
        return productRepository.findTop20ByOrgIdAndSkuContainingIgnoreCaseOrderBySkuAsc(orgId, sku);
    }

    public Product create(Product product) {
        Long orgId = orgContextResolver.currentOrgId();
        product.setOrgId(orgId);
        return productRepository.save(product);
    }

    public Product update(Long id, Product request) {
        Product product = get(id);
        product.setSku(request.getSku());
        product.setBarcode(request.getBarcode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setGst(request.getGst());
        product.setCategoryId(request.getCategoryId());
        product.setBrandId(request.getBrandId());
        product.setHsnSacCode(request.getHsnSacCode());
        product.setLowStockThreshold(request.getLowStockThreshold());
        product.setActive(request.isActive());
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = get(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
