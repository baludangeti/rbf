package com.rbf.product.catalog.service;

import com.rbf.product.catalog.model.ProductBrand;
import com.rbf.product.catalog.repository.ProductBrandRepository;
import com.rbf.product.common.tenant.OrgContextResolver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductBrandService {

    private final ProductBrandRepository repository;
    private final OrgContextResolver orgContextResolver;

    public ProductBrandService(ProductBrandRepository repository, OrgContextResolver orgContextResolver) {
        this.repository = repository;
        this.orgContextResolver = orgContextResolver;
    }

    public List<ProductBrand> list() {
        return repository.findByOrgIdOrderByNameAsc(orgContextResolver.currentOrgId());
    }

    public ProductBrand create(ProductBrand request) {
        request.setOrgId(orgContextResolver.currentOrgId());
        return repository.save(request);
    }

    public ProductBrand update(Long id, ProductBrand request) {
        ProductBrand brand = get(id);
        brand.setName(request.getName());
        brand.setCode(request.getCode());
        brand.setActive(request.isActive());
        return repository.save(brand);
    }

    public void deactivate(Long id) {
        ProductBrand brand = get(id);
        brand.setActive(false);
        repository.save(brand);
    }

    private ProductBrand get(Long id) {
        return repository.findByIdAndOrgId(id, orgContextResolver.currentOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
    }
}
