// src/main/java/com/tutorial/tarifas_service/service/TarifaService.java
package com.tutorial.tarifas_service.service;

import com.tutorial.tarifas_service.entity.Tarifa;
import com.tutorial.tarifas_service.repository.TarifaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TarifaService {
    public Integer calculateTarifa(int laps) {
        if (laps <= 10)  return 15000;
        if (laps <= 15)  return 20000;
        if (laps <= 20)  return 25000;
        return 30000;
    }
}