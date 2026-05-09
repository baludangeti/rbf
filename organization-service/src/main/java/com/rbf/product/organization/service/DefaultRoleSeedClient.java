package com.rbf.product.organization.service;

import com.rbf.product.common.web.RestClientUtil;
import com.rbf.product.common.tenant.OrgContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DefaultRoleSeedClient {

    private final RestClientUtil restClient;
    private final String authBaseUrl;

    public DefaultRoleSeedClient(RestTemplate restTemplate,
                                 @Value("${services.auth-service.base-url}") String authBaseUrl) {
        this.restClient = new RestClientUtil(restTemplate);
        this.authBaseUrl = authBaseUrl;
    }

    public void seedDefaultRoles() {
        restClient.post(authBaseUrl + "/api/roles/defaults/" + OrgContext.requireOrgId(), null, Void.class);
    }
}
