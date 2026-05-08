package com.rbf.product.auth.repository;

import com.rbf.product.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndOrgId(String username, Long orgId);

    boolean existsByUsernameAndOrgId(String username, Long orgId);

    List<User> findByOrgIdOrderByUsernameAsc(Long orgId);
}
