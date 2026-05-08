package com.rbf.product.organization.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.organization.model.Organization;
import com.rbf.product.organization.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final DefaultRoleSeedClient defaultRoleSeedClient;

    public OrganizationService(OrganizationRepository organizationRepository, DefaultRoleSeedClient defaultRoleSeedClient) {
        this.organizationRepository = organizationRepository;
        this.defaultRoleSeedClient = defaultRoleSeedClient;
    }

    public List<Organization> list() {
        return organizationRepository.findByOrgIdOrderByNameAsc(OrgContext.requireOrgId());
    }

    public Organization get(Long id) {
        return organizationRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));
    }

    public Organization create(Organization organization) {
        Long orgId = OrgContext.requireOrgId();
        if (organizationRepository.existsByCodeAndOrgId(organization.getCode(), orgId)) {
            throw new IllegalArgumentException("Organization code already exists");
        }
        organization.setOrgId(orgId);
        Organization saved = organizationRepository.save(organization);
        defaultRoleSeedClient.seedDefaultRoles();
        return saved;
    }

    public Organization update(Long id, Organization request) {
        Organization organization = get(id);
        organization.setName(request.getName());
        organization.setCode(request.getCode());
        organization.setBusinessType(request.getBusinessType());
        organization.setGstin(request.getGstin());
        organization.setPanNumber(request.getPanNumber());
        organization.setEmail(request.getEmail());
        organization.setPhone(request.getPhone());
        organization.setAddress(request.getAddress());
        organization.setCity(request.getCity());
        organization.setState(request.getState());
        organization.setCountry(request.getCountry());
        organization.setPincode(request.getPincode());
        return organizationRepository.save(organization);
    }

    public void delete(Long id) {
        Organization organization = get(id);
        organizationRepository.delete(organization);
    }
}
