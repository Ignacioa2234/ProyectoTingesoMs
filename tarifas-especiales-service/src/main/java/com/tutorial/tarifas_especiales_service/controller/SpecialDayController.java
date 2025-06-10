// src/main/java/com/tutorial/tarifas_especiales_service/controller/SpecialDayController.java
package com.tutorial.tarifas_especiales_service.controller;

import com.tutorial.tarifas_especiales_service.service.SpecialDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class SpecialDayController {

    private final SpecialDayService specialDayService;

    /**
     * Lógica original para días especiales (feriados, fines de semana, etc.)
     * GET /discount/special-day?date=2025-06-09
     */
    @GetMapping("/special-day")
    public ResponseEntity<Integer> getRate(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        int rate = specialDayService.getSpecialDayRate(date);
        return ResponseEntity.ok(rate);
    }

    /**
     * Nuevo endpoint para descuento de cumpleaños:
     * GET /discount/cumple?user=usuario
     */
    @GetMapping("/cumple")
    public ResponseEntity<Integer> getBirthdayDiscount(
            @RequestParam("user") String user
    ) {
        int discount = specialDayService.calculateBirthdayDiscount(user);
        return ResponseEntity.ok(discount);
    }
}
