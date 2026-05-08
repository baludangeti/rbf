package com.rbf.product.credit.controller;

import com.rbf.product.credit.dto.CreditAccountRequest;
import com.rbf.product.credit.dto.CreditAccountResponse;
import com.rbf.product.credit.dto.CreditSaleRequest;
import com.rbf.product.credit.dto.CreditSettlementRequest;
import com.rbf.product.credit.dto.CreditTransactionResponse;
import com.rbf.product.credit.dto.CreditValidationRequest;
import com.rbf.product.credit.dto.CreditValidationResponse;
import com.rbf.product.credit.service.CustomerCreditService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer-credits")
public class CustomerCreditController {

    private final CustomerCreditService creditService;

    public CustomerCreditController(CustomerCreditService creditService) {
        this.creditService = creditService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BILLING_VIEW') or hasAuthority('ACCOUNTING_VIEW')")
    public List<CreditAccountResponse> listAccounts() {
        return creditService.listAccounts();
    }

    @GetMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('BILLING_VIEW') or hasAuthority('ACCOUNTING_VIEW')")
    public CreditAccountResponse getByCustomer(@PathVariable Long customerId) {
        return creditService.getByCustomer(customerId);
    }

    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW') or hasAuthority('BILLING_CREATE')")
    public CreditAccountResponse createOrUpdateAccount(@Valid @RequestBody CreditAccountRequest request) {
        return creditService.createOrUpdateAccount(request);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public CreditValidationResponse validateLimit(@Valid @RequestBody CreditValidationRequest request) {
        return creditService.validateLimit(request);
    }

    @PostMapping("/sales")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public CreditAccountResponse recordCreditSale(@Valid @RequestBody CreditSaleRequest request) {
        return creditService.recordCreditSale(request);
    }

    @PostMapping("/customers/{customerId}/settlements")
    @PreAuthorize("hasAuthority('BILLING_CREATE') or hasAuthority('ACCOUNTING_VIEW')")
    public CreditAccountResponse settlePayment(@PathVariable Long customerId,
                                               @Valid @RequestBody CreditSettlementRequest request) {
        return creditService.settlePayment(customerId, request);
    }

    @GetMapping("/customers/{customerId}/transactions")
    @PreAuthorize("hasAuthority('BILLING_VIEW') or hasAuthority('ACCOUNTING_VIEW')")
    public List<CreditTransactionResponse> listTransactions(@PathVariable Long customerId) {
        return creditService.listTransactions(customerId);
    }
}
