package com.rbf.product.console.service;

import com.rbf.product.console.client.AuthServiceClient;
import com.rbf.product.console.client.OrganizationServiceClient;
import com.rbf.product.console.client.dto.AdminUserCreateRequest;
import com.rbf.product.console.client.dto.OrganizationCreateRequest;
import com.rbf.product.console.client.dto.OrganizationResponse;
import com.rbf.product.console.client.dto.UserResponse;
import com.rbf.product.console.dto.AdminRegistrationForm;
import com.rbf.product.console.dto.OrganizationRegistrationForm;
import com.rbf.product.console.dto.RegistrationResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

@Service
public class OrganizationRegistrationService {

    private final OrganizationServiceClient organizationServiceClient;
    private final AuthServiceClient authServiceClient;

    public OrganizationRegistrationService(OrganizationServiceClient organizationServiceClient,
                                           AuthServiceClient authServiceClient) {
        this.organizationServiceClient = organizationServiceClient;
        this.authServiceClient = authServiceClient;
    }

    public RegistrationResult register(OrganizationRegistrationForm organizationForm, AdminRegistrationForm adminForm) {
        Long orgId = generateOrgId();
        OrganizationResponse organization = organizationServiceClient.createOrganization(orgId, toOrganizationRequest(orgId, organizationForm));
        authServiceClient.seedDefaultRoles(orgId);
        UserResponse user = authServiceClient.createAdminUser(orgId, toAdminRequest(orgId, adminForm));
        return new RegistrationResult(orgId, user.getId(), organization.getName(), user.getUsername());
    }

    private OrganizationCreateRequest toOrganizationRequest(Long orgId, OrganizationRegistrationForm form) {
        OrganizationCreateRequest request = new OrganizationCreateRequest();
        request.setOrgId(orgId);
        request.setName(form.getOrganizationName());
        request.setCode(generateOrganizationCode(form.getOrganizationName(), orgId));
        request.setBusinessType(form.getBusinessType());
        request.setGstin(form.getGstin());
        request.setPanNumber(form.getPanNumber());
        request.setEmail(form.getEmail());
        request.setPhone(form.getPhone());
        request.setAddress(form.getAddress());
        request.setCity(form.getCity());
        request.setState(form.getState());
        request.setCountry(form.getCountry());
        request.setPincode(form.getPincode());
        return request;
    }

    private AdminUserCreateRequest toAdminRequest(Long orgId, AdminRegistrationForm form) {
        AdminUserCreateRequest request = new AdminUserCreateRequest();
        request.setOrgId(orgId);
        request.setFullName(form.getFullName());
        request.setEmail(form.getEmail());
        request.setPhone(form.getPhone());
        request.setUsername(form.getUsername());
        request.setPassword(form.getPassword());
        return request;
    }

    private Long generateOrgId() {
        return Instant.now().toEpochMilli();
    }

    private String generateOrganizationCode(String organizationName, Long orgId) {
        String prefix = organizationName == null ? "ORG" : organizationName.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");
        if (prefix.length() > 10) {
            prefix = prefix.substring(0, 10);
        }
        if (prefix.isBlank()) {
            prefix = "ORG";
        }
        return prefix + orgId.toString().substring(Math.max(0, orgId.toString().length() - 6));
    }
}
