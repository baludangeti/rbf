package com.rbf.product.payment.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.payment.dto.PaymentStatusBatchRequest;
import com.rbf.product.payment.dto.PaymentStatusResponse;
import com.rbf.product.payment.dto.RefundPaymentRequest;
import com.rbf.product.payment.dto.RecordPaymentRequest;
import com.rbf.product.payment.model.Payment;
import com.rbf.product.payment.model.PaymentStatus;
import com.rbf.product.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment recordPayment(RecordPaymentRequest request) {
        Long orgId = OrgContext.requireOrgId();
        BigDecimal paidAmount = totalPaid(request.getInvoiceId(), orgId).add(request.getAmount());
        PaymentStatus status = paidAmount.compareTo(request.getInvoiceTotal()) >= 0
                ? PaymentStatus.PAID
                : PaymentStatus.PARTIAL;

        Payment payment = new Payment();
        payment.setOrgId(orgId);
        payment.setInvoiceId(request.getInvoiceId());
        payment.setAmount(request.getAmount());
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment recordRefund(RefundPaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrgId(OrgContext.requireOrgId());
        payment.setInvoiceId(request.getInvoiceId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }

    public PaymentStatusResponse getPaymentStatus(Long invoiceId, BigDecimal invoiceTotal) {
        Long orgId = OrgContext.requireOrgId();
        BigDecimal paidAmount = totalPaid(invoiceId, orgId);
        PaymentStatus status = paidAmount.compareTo(invoiceTotal) >= 0 ? PaymentStatus.PAID : PaymentStatus.PARTIAL;
        return new PaymentStatusResponse(invoiceId, orgId, paidAmount, invoiceTotal, status);
    }

    public List<PaymentStatusResponse> getPaymentStatuses(PaymentStatusBatchRequest request) {
        return request.getInvoices().stream()
                .map(invoice -> getPaymentStatus(invoice.getInvoiceId(), invoice.getInvoiceTotal()))
                .toList();
    }

    public List<Payment> getInvoicePayments(Long invoiceId) {
        return paymentRepository.findByInvoiceIdAndOrgIdOrderByCreatedAtAsc(invoiceId, OrgContext.requireOrgId());
    }

    private BigDecimal totalPaid(Long invoiceId, Long orgId) {
        return paymentRepository.findByInvoiceIdAndOrgIdOrderByCreatedAtAsc(invoiceId, orgId).stream()
                .filter(payment -> payment.getStatus() != PaymentStatus.CANCELLED)
                .filter(payment -> payment.getStatus() != PaymentStatus.REFUNDED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
