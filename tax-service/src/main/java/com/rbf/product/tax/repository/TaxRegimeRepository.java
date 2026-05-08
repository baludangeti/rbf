package com.rbf.product.tax.repository;

import com.rbf.product.tax.model.TaxRegime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaxRegimeRepository extends JpaRepository<TaxRegime, Long> {
    List<TaxRegime> findByActiveTrueOrderByCountryCodeAscRegimeNameAsc();
    Optional<TaxRegime> findFirstByCountryCodeAndActiveTrueOrderByIdAsc(String countryCode);
}
