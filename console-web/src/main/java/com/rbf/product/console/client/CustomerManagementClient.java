package com.rbf.product.console.client;

import com.rbf.product.console.dto.customer.CustomerConsoleRequest;
import com.rbf.product.console.dto.customer.CustomerCreditLimitRequest;
import com.rbf.product.console.dto.customer.CustomerSettlementRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class CustomerManagementClient {

    private final RestTemplate restTemplate;
    private final String customerBaseUrl;
    private final String creditBaseUrl;

    public CustomerManagementClient(RestTemplate restTemplate,
                                    @Value("${services.customer-service.base-url}") String customerBaseUrl,
                                    @Value("${services.customer-credit-service.base-url}") String creditBaseUrl) {
        this.restTemplate = restTemplate;
        this.customerBaseUrl = customerBaseUrl;
        this.creditBaseUrl = creditBaseUrl;
    }

    public Map<String, Object> customers(HttpSession session, String search, int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(customerBaseUrl + "/api/customers/page")
                .queryParam("search", search)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();
        return getMap(session, url);
    }

    public Map<String, Object> customer(HttpSession session, Long id) {
        return getMap(session, customerBaseUrl + "/api/customers/" + id);
    }

    public Map<String, Object> createCustomer(HttpSession session, CustomerConsoleRequest request) {
        return exchangeMap(session, customerBaseUrl + "/api/customers", HttpMethod.POST, request);
    }

    public Map<String, Object> updateCustomer(HttpSession session, Long id, CustomerConsoleRequest request) {
        return exchangeMap(session, customerBaseUrl + "/api/customers/" + id, HttpMethod.PUT, request);
    }

    public void deactivateCustomer(HttpSession session, Long id) {
        restTemplate.exchange(customerBaseUrl + "/api/customers/" + id, HttpMethod.DELETE,
                new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public List<Map<String, Object>> creditAccounts(HttpSession session) {
        return getList(session, creditBaseUrl + "/api/customer-credits");
    }

    public Map<String, Object> saveCreditLimit(HttpSession session, CustomerCreditLimitRequest request) {
        return exchangeMap(session, creditBaseUrl + "/api/customer-credits/accounts", HttpMethod.POST, request);
    }

    public Map<String, Object> customerCredit(HttpSession session, Long customerId) {
        return getMap(session, creditBaseUrl + "/api/customer-credits/customers/" + customerId);
    }

    public List<Map<String, Object>> ledger(HttpSession session, Long customerId) {
        return getList(session, creditBaseUrl + "/api/customer-credits/customers/" + customerId + "/transactions");
    }

    public Map<String, Object> settlePayment(HttpSession session, Long customerId, CustomerSettlementRequest request) {
        return exchangeMap(session, creditBaseUrl + "/api/customer-credits/customers/" + customerId + "/settlements", HttpMethod.POST, request);
    }

    private Map<String, Object> getMap(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    private List<Map<String, Object>> getList(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();
    }

    private Map<String, Object> exchangeMap(HttpSession session, String url, HttpMethod method, Object request) {
        return restTemplate.exchange(url, method, new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }
}
