package com.rbf.product.catalog.service;

import com.rbf.product.catalog.model.ProductCategory;
import com.rbf.product.catalog.repository.ProductCategoryRepository;
import com.rbf.product.common.tenant.OrgContextResolver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository repository;
    private final OrgContextResolver orgContextResolver;

    public ProductCategoryService(ProductCategoryRepository repository, OrgContextResolver orgContextResolver) {
        this.repository = repository;
        this.orgContextResolver = orgContextResolver;
    }

    public List<ProductCategory> list() {
        return repository.findByOrgIdOrderByNameAsc(orgContextResolver.currentOrgId());
    }

    public ProductCategory create(ProductCategory request) {
        request.setOrgId(orgContextResolver.currentOrgId());
        return repository.save(request);
    }

    public ProductCategory update(Long id, ProductCategory request) {
        ProductCategory category = get(id);
        category.setName(request.getName());
        category.setHsnSacCode(request.getHsnSacCode());
        category.setActive(request.isActive());
        return repository.save(category);
    }

    public void deactivate(Long id) {
        ProductCategory category = get(id);
        category.setActive(false);
        repository.save(category);
    }

    private ProductCategory get(Long id) {
        return repository.findByIdAndOrgId(id, orgContextResolver.currentOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }
}
