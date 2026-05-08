package com.rbf.product.common.web;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.common.security.AuthTokenContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClientUtil {

    private final RestTemplate restTemplate;

    public RestClientUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(orgHeaders()),
                responseType,
                uriVariables
        );
        return response.getBody();
    }

    public <T> T post(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, orgHeaders()),
                responseType,
                uriVariables
        );
        return response.getBody();
    }

    public HttpHeaders orgHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(OrgContextFilter.ORG_HEADER, OrgContext.requireOrgId().toString());
        String token = AuthTokenContext.getToken();
        if (token != null && !token.isBlank()) {
            headers.setBearerAuth(token);
        }
        return headers;
    }
}
