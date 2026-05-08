package com.rbf.product.console.controller;

import com.rbf.product.console.client.BillingInvoiceClient;
import com.rbf.product.console.client.BillingProductClient;
import com.rbf.product.console.dto.billing.PosInvoiceRequest;
import com.rbf.product.console.dto.billing.PosInvoiceResponse;
import com.rbf.product.console.dto.billing.PosProductDto;
import com.rbf.product.console.dto.billing.PosTaxPreviewRequest;
import com.rbf.product.console.dto.billing.PosTaxPreviewResponse;
import com.rbf.product.console.service.BillingPosService;
import com.rbf.product.console.service.GstInvoicePrintService;
import com.rbf.product.console.session.SessionKeys;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/billing")
public class BillingConsoleController {

    private final BillingProductClient productClient;
    private final BillingPosService billingPosService;
    private final BillingInvoiceClient invoiceClient;
    private final GstInvoicePrintService invoicePrintService;

    public BillingConsoleController(BillingProductClient productClient,
                                    BillingPosService billingPosService,
                                    BillingInvoiceClient invoiceClient,
                                    GstInvoicePrintService invoicePrintService) {
        this.productClient = productClient;
        this.billingPosService = billingPosService;
        this.invoiceClient = invoiceClient;
        this.invoicePrintService = invoicePrintService;
    }

    @GetMapping("/pos")
    public String billingConsole(HttpSession session, Model model) {
        model.addAttribute("pageTitle", "Billing POS");
        model.addAttribute("orgId", session.getAttribute(SessionKeys.ORG_ID));
        return "billing/pos";
    }

    @GetMapping("/invoice-view")
    public String invoiceView(HttpSession session,
                              Model model,
                              @RequestParam(required = false) Long invoiceId) {
        model.addAttribute("pageTitle", "GST Invoice");
        model.addAttribute("invoiceId", invoiceId);
        if (invoiceId != null) {
            model.addAttribute("invoicePrint", invoicePrintService.loadInvoice(session, invoiceId));
        }
        return "billing/invoice-view";
    }

    @GetMapping("/invoices/{invoiceId}/view")
    public String invoiceViewByPath(HttpSession session, Model model, @PathVariable Long invoiceId) {
        model.addAttribute("pageTitle", "GST Invoice");
        model.addAttribute("invoiceId", invoiceId);
        model.addAttribute("invoicePrint", invoicePrintService.loadInvoice(session, invoiceId));
        return "billing/invoice-view";
    }

    @GetMapping("/api/products/search")
    @ResponseBody
    public List<PosProductDto> searchProducts(HttpSession session, @RequestParam String q) {
        return productClient.search(session, q);
    }

    @PostMapping("/api/tax/preview")
    @ResponseBody
    public PosTaxPreviewResponse previewTax(HttpSession session, @Valid @RequestBody PosTaxPreviewRequest request) {
        return billingPosService.previewTax(session, request);
    }

    @PostMapping("/api/invoices")
    @ResponseBody
    public PosInvoiceResponse createInvoice(HttpSession session, @Valid @RequestBody PosInvoiceRequest request) {
        return billingPosService.createInvoice(session, request);
    }

    @GetMapping("/api/invoices/{invoiceId}/pdf")
    public ResponseEntity<byte[]> downloadPdf(HttpSession session, @PathVariable Long invoiceId) {
        ResponseEntity<byte[]> response = invoiceClient.downloadPdf(session, invoiceId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice-" + invoiceId + ".pdf")
                .body(response.getBody());
    }
}
