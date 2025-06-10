package com.tutorial.descuentos_frecuentes_service.controller;

import com.tutorial.descuentos_frecuentes_service.service.FrequentDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class FrequentDiscountController {

    private final FrequentDiscountService service;

    @GetMapping("/frecuente")
    public ResponseEntity<Integer> getFrequentDiscount(
            @RequestParam("user") String user
    ) {
        int discount = service.calculateFrequentDiscount(user);
        return ResponseEntity.ok(discount);
    }
}
