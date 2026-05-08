package com.rbf.product.billing.controller;

import com.rbf.product.billing.dto.CreateInvoiceRequest;
import com.rbf.product.billing.dto.FinalizeInvoiceRequest;
import com.rbf.product.billing.dto.HoldInvoiceRequest;
import com.rbf.product.billing.dto.InvoiceResponse;
import com.rbf.product.billing.service.BillingService;
import com.rbf.product.billing.service.InvoicePdfService;
import jakarta.validation.Valid;
import com.rbf.product.billing.model.InvoiceStatus;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;
    private final InvoicePdfService invoicePdfService;

    public BillingController(BillingService billingService, InvoicePdfService invoicePdfService) {
        this.billingService = billingService;
        this.invoicePdfService = invoicePdfService;
    }

    @GetMapping("/invoices")
    public List<InvoiceResponse> listInvoices() {
        return billingService.list();
    }

    @GetMapping("/invoices/page")
    public Page<InvoiceResponse> listInvoicesPage(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "25") int size,
                                                  @RequestParam(required = false) InvoiceStatus status) {
        PageRequest pageRequest = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "createdAt"));
        if (status != null) {
            return billingService.listByStatusPage(status, pageRequest);
        }
        return billingService.listPage(pageRequest);
    }

    @GetMapping("/invoices/drafts")
    public List<InvoiceResponse> listDrafts() {
        return billingService.listDrafts();
    }

    @GetMapping("/invoices/held")
    public List<InvoiceResponse> listHeld() {
        return billingService.listHeld();
    }

    @GetMapping("/invoices/{id}")
    public InvoiceResponse getInvoice(@PathVariable Long id) {
        return billingService.get(id);
    }

    @GetMapping("/invoices/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) {
        byte[] pdf = invoicePdfService.generateInvoicePdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("invoice-" + id + ".pdf")
                                .build()
                                .toString())
                .body(pdf);
    }

    @PostMapping("/invoices")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public InvoiceResponse createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return billingService.createInvoice(request);
    }

    @PostMapping("/invoices/drafts")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public InvoiceResponse createDraft(@Valid @RequestBody CreateInvoiceRequest request) {
        return billingService.createDraft(request);
    }

    @PutMapping("/invoices/{id}/cart")
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public InvoiceResponse updateDraftCart(@PathVariable Long id, @Valid @RequestBody CreateInvoiceRequest request) {
        return billingService.updateDraftCart(id, request);
    }

    @PostMapping("/invoices/{id}/hold")
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public InvoiceResponse holdInvoice(@PathVariable Long id, @Valid @RequestBody HoldInvoiceRequest request) {
        return billingService.holdInvoice(id, request);
    }

    @PostMapping("/invoices/{id}/resume")
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public InvoiceResponse resumeInvoice(@PathVariable Long id) {
        return billingService.resumeInvoice(id);
    }

    @PostMapping("/invoices/held/{holdReference}/resume")
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public InvoiceResponse resumeInvoiceByReference(@PathVariable String holdReference) {
        return billingService.resumeInvoiceByReference(holdReference);
    }

    @PostMapping("/invoices/{id}/finalize")
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public InvoiceResponse finalizeDraft(@PathVariable Long id, @Valid @RequestBody FinalizeInvoiceRequest request) {
        return billingService.finalizeDraft(id, request);
    }
}
