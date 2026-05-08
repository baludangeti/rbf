package com.rbf.product.console.client;

import com.rbf.product.console.client.dto.LoginRequest;
import com.rbf.product.console.client.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthClient {

    private final RestTemplate restTemplate;
    private final String authServiceBaseUrl;

    public AuthClient(RestTemplate restTemplate,
                      @Value("${services.auth-service.base-url}") String authServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.authServiceBaseUrl = authServiceBaseUrl;
    }

    public LoginResponse login(Long orgId, String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(new LoginRequest(orgId, username, password), headers);
        return restTemplate.postForObject(authServiceBaseUrl + "/api/auth/login", entity, LoginResponse.class);
    }
}
