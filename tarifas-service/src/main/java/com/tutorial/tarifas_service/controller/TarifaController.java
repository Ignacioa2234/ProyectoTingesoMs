package com.tutorial.tarifas_service.controller;

import com.tutorial.tarifas_service.service.TarifaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TarifaController {

    private final TarifaService tarifaService;

    @GetMapping("/tarifa")
    public Integer getTarifa(@RequestParam("laps") int laps) {
        return tarifaService.calculateTarifa(laps);
    }
}
