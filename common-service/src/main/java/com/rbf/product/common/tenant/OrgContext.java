package com.rbf.product.common.tenant;

public final class OrgContext {

    private static final ThreadLocal<Long> CURRENT_ORG_ID = new ThreadLocal<>();

    private OrgContext() {
    }

    public static void setOrgId(Long orgId) {
        CURRENT_ORG_ID.set(orgId);
    }

    public static Long getOrgId() {
        return CURRENT_ORG_ID.get();
    }

    public static Long requireOrgId() {
        Long orgId = getOrgId();
        if (orgId == null) {
            throw new IllegalStateException("Missing org context");
        }
        return orgId;
    }

    public static void clear() {
        CURRENT_ORG_ID.remove();
    }
}
