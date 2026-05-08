package com.rbf.product.console.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ConsoleAjaxExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Map<String, Object>> handleDownstream(HttpStatusCodeException ex,
                                                                HttpServletRequest request) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(error(ex.getStatusCode().value(), downstreamMessage(ex), request.getRequestURI()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleServiceUnavailable(ResourceAccessException ex,
                                                                        HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(error(503, "Backend service is unavailable.", request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneric(Exception ex, HttpServletRequest request) throws Exception {
        if (!isAjaxRequest(request)) {
            throw ex;
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error(500, "Unexpected console error.", request.getRequestURI()));
    }

    private Map<String, Object> error(int status, String message, String path) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", false);
        response.put("timestamp", Instant.now().toString());
        response.put("status", status);
        response.put("message", message);
        response.put("path", path);
        return response;
    }

    private String downstreamMessage(HttpStatusCodeException ex) {
        if (ex.getStatusCode().value() == 401) {
            return "Unauthorized. Please login again.";
        }
        if (ex.getStatusCode().value() == 403) {
            return "You do not have permission for this action.";
        }
        if (ex.getStatusCode().value() >= 500) {
            return "Backend service error.";
        }
        return ex.getResponseBodyAsString() == null || ex.getResponseBodyAsString().isBlank()
                ? "Request failed."
                : ex.getResponseBodyAsString();
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader("Accept");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith)
                || (accept != null && accept.contains("application/json"))
                || request.getRequestURI().contains("/api/")
                || request.getRequestURI().contains("/gateway/");
    }
}
