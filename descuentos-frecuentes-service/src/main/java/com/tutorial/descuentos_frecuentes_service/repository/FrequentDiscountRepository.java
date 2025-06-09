package com.tutorial.descuentos_frecuentes_service.repository;

import com.tutorial.descuentos_frecuentes_service.entity.FrequentDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface FrequentDiscountRepository extends JpaRepository<FrequentDiscount, Long> {

    Optional<FrequentDiscount> findByUsername(String username);
}
