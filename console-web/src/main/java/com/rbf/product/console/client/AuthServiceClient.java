package com.rbf.product.console.client;

import com.rbf.product.console.client.dto.AdminUserCreateRequest;
import com.rbf.product.console.client.dto.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AuthServiceClient(RestTemplate restTemplate,
                             @Value("${services.auth-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public UserResponse createAdminUser(Long orgId, AdminUserCreateRequest request) {
        HttpEntity<AdminUserCreateRequest> entity = new HttpEntity<>(request, jsonHeaders(orgId));
        return restTemplate.postForObject(baseUrl + "/api/users/admin", entity, UserResponse.class);
    }

    public void seedDefaultRoles(Long orgId) {
        HttpEntity<Void> entity = new HttpEntity<>(jsonHeaders(orgId));
        restTemplate.postForEntity(baseUrl + "/api/roles/defaults/" + orgId, entity, Void.class);
    }

    private HttpHeaders jsonHeaders(Long orgId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-ORG-ID", String.valueOf(orgId));
        return headers;
    }
}
