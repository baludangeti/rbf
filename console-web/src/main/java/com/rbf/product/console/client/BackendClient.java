package com.rbf.product.console.client;

import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class BackendClient {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
            new ParameterizedTypeReference<>() {
            };
    private static final ParameterizedTypeReference<List<Map<String, Object>>> LIST_TYPE =
            new ParameterizedTypeReference<>() {
            };

    private final RestTemplate restTemplate;

    public BackendClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getMap(HttpSession session, String url) {
        return exchangeMap(session, url, HttpMethod.GET, null);
    }

    public List<Map<String, Object>> getList(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)), LIST_TYPE).getBody();
    }

    public Map<String, Object> exchangeMap(HttpSession session, String url, HttpMethod method, Object body) {
        return restTemplate.exchange(url, method,
                new HttpEntity<>(body, BackendHeaders.json(session)), MAP_TYPE).getBody();
    }

    public void exchangeVoid(HttpSession session, String url, HttpMethod method) {
        restTemplate.exchange(url, method, new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }
}
