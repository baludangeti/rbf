package com.rbf.product.order.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.order.client.ProductClient;
import com.rbf.product.order.client.ProductResponse;
import com.rbf.product.order.model.CreateOrderRequest;
import com.rbf.product.order.model.CustomerOrder;
import com.rbf.product.order.repository.CustomerOrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerOrderService {

    private final CustomerOrderRepository orderRepository;
    private final ProductClient productClient;

    public CustomerOrderService(CustomerOrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    public List<CustomerOrder> list() {
        return orderRepository.findByOrgIdOrderByCreatedAtDesc(OrgContext.requireOrgId());
    }

    public CustomerOrder get(Long id) {
        return orderRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    public CustomerOrder create(CreateOrderRequest request) {
        ProductResponse product = productClient.getActiveProduct(request.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        CustomerOrder order = new CustomerOrder();
        order.setOrgId(OrgContext.requireOrgId());
        order.setProductId(product.getId());
        order.setProductName(product.getName());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(product.getPrice());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        return orderRepository.save(order);
    }
}
