package com.rbf.product.order.repository;

import com.rbf.product.order.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByOrgIdOrderByCreatedAtDesc(Long orgId);

    Optional<CustomerOrder> findByIdAndOrgId(Long id, Long orgId);
}
