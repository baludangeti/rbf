package com.rbf.product.console.client;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AdminOrganizationClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AdminOrganizationClient(RestTemplate restTemplate,
                                   @Value("${services.organization-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Map<String, Object>> listOrganizations(HttpSession session) {
        return restTemplate.exchange(
                baseUrl + "/api/organizations",
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();
    }

    public Map<String, Object> updateOrganization(HttpSession session, Long id, Map<String, Object> request) {
        return restTemplate.exchange(
                baseUrl + "/api/organizations/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }
}
