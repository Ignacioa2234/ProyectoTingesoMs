package com.tutorial.tarifas_especiales_service.repository;

import com.tutorial.tarifas_especiales_service.entity.SpecialDayRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SpecialDayRateRepository
        extends JpaRepository<SpecialDayRate, Long> {

    Optional<SpecialDayRate> findByDate(LocalDate date);
}
