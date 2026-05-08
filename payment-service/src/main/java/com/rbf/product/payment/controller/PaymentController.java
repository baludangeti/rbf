package com.rbf.product.payment.controller;

import com.rbf.product.payment.dto.PaymentStatusResponse;
import com.rbf.product.payment.dto.PaymentStatusBatchRequest;
import com.rbf.product.payment.dto.RefundPaymentRequest;
import com.rbf.product.payment.dto.RecordPaymentRequest;
import com.rbf.product.payment.model.Payment;
import com.rbf.product.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment recordPayment(@Valid @RequestBody RecordPaymentRequest request) {
        return paymentService.recordPayment(request);
    }

    @PostMapping("/refunds")
    @ResponseStatus(HttpStatus.CREATED)
    public Payment recordRefund(@Valid @RequestBody RefundPaymentRequest request) {
        return paymentService.recordRefund(request);
    }

    @GetMapping("/invoice/{invoiceId}")
    public List<Payment> getInvoicePayments(@PathVariable Long invoiceId) {
        return paymentService.getInvoicePayments(invoiceId);
    }

    @GetMapping("/invoice/{invoiceId}/status")
    public PaymentStatusResponse getPaymentStatus(@PathVariable Long invoiceId,
                                                  @RequestParam BigDecimal invoiceTotal) {
        return paymentService.getPaymentStatus(invoiceId, invoiceTotal);
    }

    @PostMapping("/invoice/statuses")
    public List<PaymentStatusResponse> getPaymentStatuses(@Valid @RequestBody PaymentStatusBatchRequest request) {
        return paymentService.getPaymentStatuses(request);
    }
}
