package com.rbf.product.console.client;

import com.rbf.product.console.session.SessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public final class BackendHeaders {

    private BackendHeaders() {
    }

    public static HttpHeaders json(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Object orgId = session.getAttribute(SessionKeys.ORG_ID);
        Object token = session.getAttribute(SessionKeys.JWT_TOKEN);
        if (orgId != null) {
            headers.set("X-ORG-ID", String.valueOf(orgId));
        }
        if (token != null) {
            headers.setBearerAuth(String.valueOf(token));
        }
        return headers;
    }
}
