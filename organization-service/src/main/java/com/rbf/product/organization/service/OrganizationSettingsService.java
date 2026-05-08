package com.rbf.product.organization.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.organization.model.PaymentModeSetting;
import com.rbf.product.organization.model.StoreBranch;
import com.rbf.product.organization.model.UserPreferenceSetting;
import com.rbf.product.organization.repository.PaymentModeSettingRepository;
import com.rbf.product.organization.repository.StoreBranchRepository;
import com.rbf.product.organization.repository.UserPreferenceSettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationSettingsService {
    private final StoreBranchRepository storeRepository;
    private final PaymentModeSettingRepository paymentRepository;
    private final UserPreferenceSettingRepository preferenceRepository;

    public OrganizationSettingsService(StoreBranchRepository storeRepository,
                                       PaymentModeSettingRepository paymentRepository,
                                       UserPreferenceSettingRepository preferenceRepository) {
        this.storeRepository = storeRepository;
        this.paymentRepository = paymentRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public List<StoreBranch> stores() { return storeRepository.findByOrgIdOrderByStoreNameAsc(OrgContext.requireOrgId()); }
    public StoreBranch saveStore(StoreBranch request) {
        request.setOrgId(OrgContext.requireOrgId());
        return storeRepository.save(request);
    }
    public List<PaymentModeSetting> paymentModes() { return paymentRepository.findByOrgIdOrderByModeNameAsc(OrgContext.requireOrgId()); }
    public PaymentModeSetting savePaymentMode(PaymentModeSetting request) {
        request.setOrgId(OrgContext.requireOrgId());
        return paymentRepository.save(request);
    }
    public UserPreferenceSetting preferences() {
        Long orgId = OrgContext.requireOrgId();
        return preferenceRepository.findByOrgId(orgId).orElseGet(() -> {
            UserPreferenceSetting settings = new UserPreferenceSetting();
            settings.setOrgId(orgId);
            return preferenceRepository.save(settings);
        });
    }
    public UserPreferenceSetting savePreferences(UserPreferenceSetting request) {
        UserPreferenceSetting settings = preferences();
        settings.setDefaultTheme(request.getDefaultTheme());
        settings.setDefaultPageSize(request.getDefaultPageSize());
        settings.setDateFormat(request.getDateFormat());
        settings.setCurrencySymbol(request.getCurrencySymbol());
        return preferenceRepository.save(settings);
    }
}
