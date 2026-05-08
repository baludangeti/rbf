package com.rbf.product.credit.repository;

import com.rbf.product.credit.model.CustomerCreditAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerCreditAccountRepository extends JpaRepository<CustomerCreditAccount, Long> {

    List<CustomerCreditAccount> findByOrgIdOrderByCustomerNameAsc(Long orgId);

    Optional<CustomerCreditAccount> findByIdAndOrgId(Long id, Long orgId);

    Optional<CustomerCreditAccount> findByCustomerIdAndOrgId(Long customerId, Long orgId);
}
