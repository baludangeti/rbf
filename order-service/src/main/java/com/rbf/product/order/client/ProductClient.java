package com.rbf.product.order.client;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.common.web.OrgContextFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String productServiceBaseUrl;

    public ProductClient(RestTemplate restTemplate,
                         @Value("${services.product-service.base-url}") String productServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.productServiceBaseUrl = productServiceBaseUrl;
    }

    public ProductResponse getActiveProduct(Long productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(OrgContextFilter.ORG_HEADER, OrgContext.requireOrgId().toString());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                productServiceBaseUrl + "/api/products/{id}/active",
                HttpMethod.GET,
                entity,
                ProductResponse.class,
                productId
        ).getBody();
    }
}
