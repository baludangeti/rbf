package com.rbf.product.tax.repository;

import com.rbf.product.tax.model.TaxCountry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxCountryRepository extends JpaRepository<TaxCountry, Long> {
    List<TaxCountry> findByActiveTrueOrderByCountryNameAsc();
}
