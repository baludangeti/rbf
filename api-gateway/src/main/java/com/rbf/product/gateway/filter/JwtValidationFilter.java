package com.rbf.product.gateway.filter;

import com.rbf.product.common.web.OrgContextFilter;
import com.rbf.product.gateway.config.GatewayRoutesProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    private final GatewayRoutesProperties properties;
    private final JwtUtil jwtUtil;

    public JwtValidationFilter(GatewayRoutesProperties properties, JwtUtil jwtUtil) {
        this.properties = properties;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!properties.getJwt().isEnabled() || isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing bearer token");
            return;
        }

        String token = authorization.substring(7);
        try {
            JwtClaims claims = jwtUtil.validateAndParse(token);
            request.setAttribute(OrgContextFilter.ORG_HEADER, claims.getOrgId().toString());
            request.setAttribute("X-USER-ID", claims.getUserId().toString());
            request.setAttribute("X-USERNAME", claims.getUsername());
            request.setAttribute("X-ROLES", String.join(",", claims.getRoles()));
            request.setAttribute("X-PERMISSIONS", String.join(",", claims.getPermissions()));
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/auth")
                || uri.startsWith("/api/auth")
                || uri.startsWith("/actuator")
                || ("POST".equalsIgnoreCase(request.getMethod()) && uri.equals("/api/organizations"));
    }
}
