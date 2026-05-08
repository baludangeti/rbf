package com.rbf.product.console.controller;

import com.rbf.product.console.session.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

@Controller
public class GatewayAjaxProxyController {

    private final RestTemplate restTemplate;
    private final String gatewayBaseUrl;

    public GatewayAjaxProxyController(RestTemplate restTemplate,
                                      @Value("${services.gateway.base-url}") String gatewayBaseUrl) {
        this.restTemplate = restTemplate;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    @RequestMapping("/gateway/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request,
                                        HttpSession session,
                                        @RequestBody(required = false) byte[] body) {
        String downstreamPath = request.getRequestURI()
                .substring(request.getContextPath().length() + "/gateway".length());
        String query = request.getQueryString() == null ? "" : "?" + request.getQueryString();

        HttpHeaders headers = new HttpHeaders();
        String orgId = request.getHeader("X-ORG-ID");
        if (orgId == null && session.getAttribute(SessionKeys.ORG_ID) != null) {
            orgId = String.valueOf(session.getAttribute(SessionKeys.ORG_ID));
        }
        if (orgId != null) {
            headers.set("X-ORG-ID", orgId);
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null && session.getAttribute(SessionKeys.JWT_TOKEN) != null) {
            authorization = "Bearer " + session.getAttribute(SessionKeys.JWT_TOKEN);
        }
        if (authorization != null) {
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
        if (request.getContentType() != null) {
            headers.set(HttpHeaders.CONTENT_TYPE, request.getContentType());
        }

        HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);
        try {
            return restTemplate.exchange(
                    gatewayBaseUrl + downstreamPath + query,
                    HttpMethod.valueOf(request.getMethod()),
                    entity,
                    byte[].class
            );
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .headers(ex.getResponseHeaders())
                    .body(ex.getResponseBodyAsByteArray());
        }
    }
}
