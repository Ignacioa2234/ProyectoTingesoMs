// src/main/java/com/tutorial/tarifas_service/repository/TarifaRepository.java
package com.tutorial.tarifas_service.repository;

import com.tutorial.tarifas_service.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    Optional<Tarifa> findFirstByMinLapsLessThanEqualAndMaxLapsGreaterThanEqual(int laps1, int laps2);
}
