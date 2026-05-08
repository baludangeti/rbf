package com.rbf.product.console.client;

import com.rbf.product.console.client.dto.OrganizationCreateRequest;
import com.rbf.product.console.client.dto.OrganizationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public OrganizationServiceClient(RestTemplate restTemplate,
                                     @Value("${services.organization-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public OrganizationResponse createOrganization(Long orgId, OrganizationCreateRequest request) {
        HttpHeaders headers = jsonHeaders(orgId);
        HttpEntity<OrganizationCreateRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(baseUrl + "/api/organizations", entity, OrganizationResponse.class);
    }

    private HttpHeaders jsonHeaders(Long orgId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-ORG-ID", String.valueOf(orgId));
        return headers;
    }
}
