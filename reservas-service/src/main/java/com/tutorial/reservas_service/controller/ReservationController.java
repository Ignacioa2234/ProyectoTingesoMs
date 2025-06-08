package com.tutorial.reservas_service.controller;

import com.tutorial.reservas_service.entity.ReservationEntity;
import com.tutorial.reservas_service.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationEntity> all() {
        return service.getAllReservations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationEntity> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReservationById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationEntity> create(@RequestBody ReservationEntity r) {
        return ResponseEntity.status(201).body(service.createReservation(r));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationEntity> update(
            @PathVariable Long id,
            @RequestBody ReservationEntity r) {
        return ResponseEntity.ok(service.updateReservation(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/between")
    public List<ReservationEntity> between(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        // parse your dates or adapt parameters to LocalDateTime
        // return service.findReservationsBetween(parsedStart, parsedEnd);
        return List.of(); // placeholder
    }
}
