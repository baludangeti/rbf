package com.rbf.product.credit.repository;

import com.rbf.product.credit.model.CustomerCreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerCreditTransactionRepository extends JpaRepository<CustomerCreditTransaction, Long> {

    List<CustomerCreditTransaction> findByCustomerIdAndOrgIdOrderByCreatedAtAsc(Long customerId, Long orgId);
}
