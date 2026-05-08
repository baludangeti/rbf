package com.rbf.product.common.web;

import com.rbf.product.common.tenant.OrgContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class OrgContextFilter extends OncePerRequestFilter {

    public static final String ORG_HEADER = "X-ORG-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String orgHeader = request.getHeader(ORG_HEADER);
        if (orgHeader == null || orgHeader.isBlank()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing X-Org-Id header");
            return;
        }

        try {
            OrgContext.setOrgId(Long.parseLong(orgHeader));
            filterChain.doFilter(request, response);
        } catch (NumberFormatException ex) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid X-Org-Id header");
        } finally {
            OrgContext.clear();
        }
    }
}
