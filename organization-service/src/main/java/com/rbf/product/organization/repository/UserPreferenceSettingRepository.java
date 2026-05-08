package com.rbf.product.organization.repository;

import com.rbf.product.organization.model.UserPreferenceSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceSettingRepository extends JpaRepository<UserPreferenceSetting, Long> {
    Optional<UserPreferenceSetting> findByOrgId(Long orgId);
}
