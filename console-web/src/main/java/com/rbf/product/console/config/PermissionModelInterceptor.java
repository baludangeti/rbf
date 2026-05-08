package com.rbf.product.console.config;

import com.rbf.product.console.session.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Set;

@Component
public class PermissionModelInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView == null) {
            return;
        }
        HttpSession session = request.getSession(false);
        Collection<?> permissions = session == null ? Set.of() : asCollection(session.getAttribute(SessionKeys.PERMISSIONS));
        Object orgName = session == null ? null : session.getAttribute(SessionKeys.ORG_NAME);
        Object orgId = session == null ? null : session.getAttribute(SessionKeys.ORG_ID);

        modelAndView.addObject("organizationName", orgName == null ? orgId : orgName);

        modelAndView.addObject("canCreateProduct", permissions.contains("PRODUCT_CREATE"));
        modelAndView.addObject("canUpdateProduct", permissions.contains("PRODUCT_UPDATE"));
        modelAndView.addObject("canDeleteProduct", permissions.contains("PRODUCT_DELETE"));
        modelAndView.addObject("canCreateBilling", permissions.contains("BILLING_CREATE"));
        modelAndView.addObject("canViewBilling", permissions.contains("BILLING_VIEW"));
        modelAndView.addObject("canUpdateInventory", permissions.contains("INVENTORY_UPDATE"));
        modelAndView.addObject("canViewReports", permissions.contains("REPORT_VIEW"));
        modelAndView.addObject("canViewAccounting", permissions.contains("ACCOUNTING_VIEW"));
        modelAndView.addObject("canManageUsers", permissions.contains("USER_MANAGE"));
        modelAndView.addObject("canManageCustomers", permissions.contains("BILLING_CREATE") || permissions.contains("ACCOUNTING_VIEW"));
        modelAndView.addObject("canViewCustomers", permissions.contains("BILLING_VIEW") || permissions.contains("BILLING_CREATE") || permissions.contains("ACCOUNTING_VIEW"));
        modelAndView.addObject("canManageSuppliers", permissions.contains("INVENTORY_UPDATE") || permissions.contains("ACCOUNTING_VIEW"));
        modelAndView.addObject("canViewSuppliers", permissions.contains("INVENTORY_UPDATE") || permissions.contains("ACCOUNTING_VIEW"));
    }

    private Collection<?> asCollection(Object permissions) {
        return permissions instanceof Collection<?> collection ? collection : Set.of();
    }
}
