package com.rbf.product.common.tenant;

import org.springframework.stereotype.Component;

@Component
public class OrgContextResolver {

    public Long currentOrgId() {
        return OrgContext.requireOrgId();
    }
}
