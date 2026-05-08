package com.rbf.product.organization.repository;

import com.rbf.product.organization.model.PaymentModeSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentModeSettingRepository extends JpaRepository<PaymentModeSetting, Long> {
    List<PaymentModeSetting> findByOrgIdOrderByModeNameAsc(Long orgId);
}
