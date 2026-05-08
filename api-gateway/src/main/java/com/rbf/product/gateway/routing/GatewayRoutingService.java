package com.rbf.product.gateway.routing;

import com.rbf.product.common.web.OrgContextFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

@Service
public class GatewayRoutingService {

    private static final Set<String> HOP_BY_HOP_HEADERS = Set.of(
            HttpHeaders.CONNECTION,
            "Keep-Alive",
            HttpHeaders.PROXY_AUTHENTICATE,
            HttpHeaders.PROXY_AUTHORIZATION,
            "TE",
            HttpHeaders.TRAILER,
            HttpHeaders.TRANSFER_ENCODING,
            HttpHeaders.UPGRADE
    );

    private final RestTemplate restTemplate;
    private final RouteResolver routeResolver;

    public GatewayRoutingService(RestTemplate restTemplate, RouteResolver routeResolver) {
        this.restTemplate = restTemplate;
        this.routeResolver = routeResolver;
    }

    public ResponseEntity<byte[]> forward(HttpServletRequest request, byte[] body) {
        return routeResolver.resolve(request.getRequestURI())
                .map(route -> forwardToRoute(route, request, body))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("No gateway route for " + request.getRequestURI()).getBytes()));
    }

    private ResponseEntity<byte[]> forwardToRoute(ResolvedRoute resolvedRoute, HttpServletRequest request, byte[] body) {
        URI targetUri = buildTargetUri(resolvedRoute, request.getQueryString());
        HttpHeaders headers = copyHeaders(request);
        HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(
                    targetUri,
                    HttpMethod.valueOf(request.getMethod()),
                    entity,
                    byte[].class
            );
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .headers(filterResponseHeaders(ex.getResponseHeaders()))
                    .body(ex.getResponseBodyAsByteArray());
        }
    }

    private URI buildTargetUri(ResolvedRoute resolvedRoute, String queryString) {
        StringBuilder uri = new StringBuilder(resolvedRoute.getRoute().getTargetUrl());
        String downstreamPath = resolvedRoute.getDownstreamPath();
        if (!downstreamPath.startsWith("/")) {
            uri.append("/");
        }
        uri.append(downstreamPath);
        if (queryString != null && !queryString.isBlank()) {
            uri.append("?").append(queryString);
        }
        return URI.create(uri.toString());
    }

    private HttpHeaders copyHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).stream()
                .filter(name -> HOP_BY_HOP_HEADERS.stream().noneMatch(blocked -> blocked.equalsIgnoreCase(name)))
                .forEach(name -> headers.put(name, Collections.list(request.getHeaders(name))));
        copyAttributeHeader(request, headers, OrgContextFilter.ORG_HEADER);
        copyAttributeHeader(request, headers, "X-USER-ID");
        copyAttributeHeader(request, headers, "X-USERNAME");
        copyAttributeHeader(request, headers, "X-ROLES");
        copyAttributeHeader(request, headers, "X-PERMISSIONS");
        return headers;
    }

    private void copyAttributeHeader(HttpServletRequest request, HttpHeaders headers, String name) {
        Object value = request.getAttribute(name);
        if (value != null) {
            headers.set(name, value.toString());
        }
    }

    private HttpHeaders filterResponseHeaders(HttpHeaders source) {
        HttpHeaders headers = new HttpHeaders();
        if (source == null) {
            return headers;
        }
        source.forEach((name, values) -> {
            if (HOP_BY_HOP_HEADERS.stream().noneMatch(blocked -> blocked.equalsIgnoreCase(name))) {
                headers.put(name, values);
            }
        });
        return headers;
    }
}
