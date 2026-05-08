package com.rbf.product.common.logging;

import com.rbf.product.common.web.OrgContextFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startedAt = System.currentTimeMillis();
        String method = request.getMethod();
        String path = request.getRequestURI();
        String orgId = request.getHeader(OrgContextFilter.ORG_HEADER);

        log.info("API request method={} path={} orgId={}", method, path, orgId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - startedAt;
            int status = response.getStatus();

            if (status >= 500) {
                log.error("API response method={} path={} status={} durationMs={} orgId={}",
                        method, path, status, durationMs, orgId);
            } else if (status >= 400) {
                log.warn("API response method={} path={} status={} durationMs={} orgId={}",
                        method, path, status, durationMs, orgId);
            } else {
                log.info("API response method={} path={} status={} durationMs={} orgId={}",
                        method, path, status, durationMs, orgId);
            }
        }
    }
}
