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

    // 1) Obtener todas las reservas
    @GetMapping
    public List<ReservationEntity> getAll() {
        return service.getAllReservations();
    }

    // 2) Buscar reservas por usuario (nombre extraído del email)
    @GetMapping("/by-user")
    public List<ReservationEntity> findByUser(@RequestParam("user") String user) {
        return service.findByUsername(user);
    }

    // 3) Obtener una reserva por id
    @GetMapping("/{id}")
    public ResponseEntity<ReservationEntity> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReservationById(id));
    }

    // 4) Crear nueva reserva
    @PostMapping
    public ResponseEntity<ReservationEntity> create(@RequestBody ReservationEntity r) {
        ReservationEntity saved = service.createReservation(r);
        return ResponseEntity.status(201).body(saved);
    }

    // 5) Actualizar reserva existente
    @PutMapping("/{id}")
    public ResponseEntity<ReservationEntity> update(
            @PathVariable Long id,
            @RequestBody ReservationEntity r) {
        return ResponseEntity.ok(service.updateReservation(id, r));
    }

    // 6) Borrar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
