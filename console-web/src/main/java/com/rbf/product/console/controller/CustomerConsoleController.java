package com.rbf.product.console.controller;

import com.rbf.product.console.client.CustomerManagementClient;
import com.rbf.product.console.dto.customer.CustomerConsoleRequest;
import com.rbf.product.console.dto.customer.CustomerCreditLimitRequest;
import com.rbf.product.console.dto.customer.CustomerSettlementRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerConsoleController {

    private final CustomerManagementClient customerClient;

    public CustomerConsoleController(CustomerManagementClient customerClient) {
        this.customerClient = customerClient;
    }

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("pageTitle", "Customers");
        return "customer/customers";
    }

    @GetMapping("/customer-ledger")
    public String customerLedger(Model model) {
        model.addAttribute("pageTitle", "Customer Ledger");
        return "customer/customer-ledger";
    }

    @GetMapping("/customer-credit")
    public String customerCredit(Model model) {
        model.addAttribute("pageTitle", "Customer Credit");
        return "customer/customer-credit";
    }

    @GetMapping("/api/customers")
    @ResponseBody
    public Map<String, Object> customersData(HttpSession session,
                                             @RequestParam(required = false) String search,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return customerClient.customers(session, search, page, size);
    }

    @GetMapping("/api/customers/{id}")
    @ResponseBody
    public Map<String, Object> customer(HttpSession session, @PathVariable Long id) {
        return customerClient.customer(session, id);
    }

    @PostMapping("/api/customers")
    @ResponseBody
    public Map<String, Object> createCustomer(HttpSession session, @Valid @RequestBody CustomerConsoleRequest request) {
        return customerClient.createCustomer(session, request);
    }

    @PutMapping("/api/customers/{id}")
    @ResponseBody
    public Map<String, Object> updateCustomer(HttpSession session,
                                              @PathVariable Long id,
                                              @Valid @RequestBody CustomerConsoleRequest request) {
        return customerClient.updateCustomer(session, id, request);
    }

    @DeleteMapping("/api/customers/{id}")
    @ResponseBody
    public void deactivateCustomer(HttpSession session, @PathVariable Long id) {
        customerClient.deactivateCustomer(session, id);
    }

    @GetMapping("/api/credits")
    @ResponseBody
    public List<Map<String, Object>> creditAccounts(HttpSession session) {
        return customerClient.creditAccounts(session);
    }

    @PostMapping("/api/credits")
    @ResponseBody
    public Map<String, Object> saveCreditLimit(HttpSession session, @Valid @RequestBody CustomerCreditLimitRequest request) {
        return customerClient.saveCreditLimit(session, request);
    }

    @GetMapping("/api/customers/{customerId}/credit")
    @ResponseBody
    public Map<String, Object> customerCredit(HttpSession session, @PathVariable Long customerId) {
        return customerClient.customerCredit(session, customerId);
    }

    @GetMapping("/api/customers/{customerId}/ledger")
    @ResponseBody
    public List<Map<String, Object>> ledger(HttpSession session, @PathVariable Long customerId) {
        return customerClient.ledger(session, customerId);
    }

    @PostMapping("/api/customers/{customerId}/settlements")
    @ResponseBody
    public Map<String, Object> settlePayment(HttpSession session,
                                             @PathVariable Long customerId,
                                             @Valid @RequestBody CustomerSettlementRequest request) {
        return customerClient.settlePayment(session, customerId, request);
    }
}
