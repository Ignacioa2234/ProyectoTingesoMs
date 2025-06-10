// src/main/java/com/tutorial/tarifas_especiales_service/service/SpecialDayService.java
package com.tutorial.tarifas_especiales_service.service;

import com.tutorial.tarifas_especiales_service.entity.SpecialDayRate;
import com.tutorial.tarifas_especiales_service.repository.SpecialDayRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SpecialDayService {

    private final SpecialDayRateRepository repo;

    private static final int WEEKEND_RATE = 20;

    /**
     * Lógica original: devuelve tasa para fechas especiales o fines de semana.
     */
    public int getSpecialDayRate(LocalDate date) {
        return repo.findByDate(date)
                .map(SpecialDayRate::getRatePct)
                .orElseGet(() -> {
                    DayOfWeek dw = date.getDayOfWeek();
                    if (dw == DayOfWeek.SATURDAY || dw == DayOfWeek.SUNDAY) {
                        return WEEKEND_RATE;
                    }
                    return 0;
                });
    }

    /**
     * Nuevo método para descuento de cumpleaños.
     * Aquí puedes implementar tu lógica real (BD de usuarios, comparar fecha de nacimiento, etc.).
     * Por ahora retorna un valor fijo o 0 si no aplica.
     */
    public int calculateBirthdayDiscount(String user) {
        // TODO: buscar fecha de nacimiento de "user" y comparar con LocalDate.now()
        // Ejemplo provisional: 50% si el usuario cumple hoy, 0 en otro caso.
        //   boolean isBirthday = ...
        //   return isBirthday ? 50 : 0;
        return 0;
    }
}
