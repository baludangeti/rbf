package com.rbf.product.payment.repository;

import com.rbf.product.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceIdAndOrgIdOrderByCreatedAtAsc(Long invoiceId, Long orgId);
}
