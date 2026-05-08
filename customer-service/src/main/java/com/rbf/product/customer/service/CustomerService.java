package com.rbf.product.customer.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.customer.model.Customer;
import com.rbf.product.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Page<Customer> page(String search, Pageable pageable) {
        String resolvedSearch = search == null || search.isBlank() ? null : search.trim();
        return repository.search(OrgContext.requireOrgId(), resolvedSearch, pageable);
    }

    public Customer get(Long id) {
        return repository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    public Customer create(Customer request) {
        request.setOrgId(OrgContext.requireOrgId());
        return repository.save(request);
    }

    public Customer update(Long id, Customer request) {
        Customer customer = get(id);
        customer.setCustomerName(request.getCustomerName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setGstin(request.getGstin());
        customer.setBillingAddress(request.getBillingAddress());
        customer.setShippingAddress(request.getShippingAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setCountry(request.getCountry());
        customer.setPincode(request.getPincode());
        customer.setActive(request.isActive());
        return repository.save(customer);
    }

    public void deactivate(Long id) {
        Customer customer = get(id);
        customer.setActive(false);
        repository.save(customer);
    }
}
