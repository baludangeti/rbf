package com.rbf.product.console.client;

import com.rbf.product.console.dto.billing.PosProductDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;

@Component
public class BillingProductClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BillingProductClient(RestTemplate restTemplate,
                                @Value("${services.product-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<PosProductDto> search(HttpSession session, String query) {
        String value = query == null ? "" : query.trim();
        if (value.isBlank()) {
            return List.of();
        }

        PosProductDto exact = findExact(session, value);
        if (exact != null) {
            return List.of(exact);
        }

        List<PosProductDto> products = restTemplate.exchange(
                baseUrl + "/api/products",
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<PosProductDto>>() {
                }).getBody();
        String needle = value.toLowerCase(Locale.ROOT);
        return products == null ? List.of() : products.stream()
                .filter(product -> contains(product.getName(), needle)
                        || contains(product.getSku(), needle)
                        || contains(product.getBarcode(), needle))
                .limit(20)
                .toList();
    }

    private PosProductDto findExact(HttpSession session, String value) {
        try {
            return restTemplate.exchange(
                    baseUrl + "/api/products/barcode/" + value,
                    HttpMethod.GET,
                    new HttpEntity<>(BackendHeaders.json(session)),
                    PosProductDto.class).getBody();
        } catch (RestClientException ignored) {
            try {
                return restTemplate.exchange(
                        baseUrl + "/api/products/sku/" + value,
                        HttpMethod.GET,
                        new HttpEntity<>(BackendHeaders.json(session)),
                        PosProductDto.class).getBody();
            } catch (RestClientException ignoredAgain) {
                return null;
            }
        }
    }

    private boolean contains(String value, String needle) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(needle);
    }
}
