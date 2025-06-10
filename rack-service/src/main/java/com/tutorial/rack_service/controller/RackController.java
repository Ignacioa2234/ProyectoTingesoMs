package com.tutorial.rack_service.controller;

import com.tutorial.rack_service.entity.RackSlot;
import com.tutorial.rack_service.service.RackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/rack")
public class RackController {

    private final RackService service;

    public RackController(RackService service) {
        this.service = service;
    }

    // 1) Inicializar rack semanal (solo usar una vez o al reiniciar)
    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeRack() {
        service.initializeWeeklyRack();
        return ResponseEntity.ok().build();
    }

    // 2) Obtener rack de un día
    @GetMapping("/day/{day}")
    public List<RackSlot> getDayRack(@PathVariable("day") DayOfWeek day) {
        return service.getDayRack(day);
    }

    // 3) Ocupar slot
    @PostMapping("/occupy")
    public RackSlot occupy(
            @RequestParam DayOfWeek day,
            @RequestParam LocalTime startTime,
            @RequestParam String reservationCode,
            @RequestParam String customerName
    ) {
        return service.occupySlot(day, startTime, reservationCode, customerName);
    }

    // 4) Liberar slot
    @PostMapping("/free")
    public RackSlot free(
            @RequestParam DayOfWeek day,
            @RequestParam LocalTime startTime
    ) {
        return service.freeSlot(day, startTime);
    }

    // 5) Obtener todos los slots ocupados
    @GetMapping("/occupied")
    public List<RackSlot> getOccupied() {
        return service.getOccupiedSlots();
    }
}
