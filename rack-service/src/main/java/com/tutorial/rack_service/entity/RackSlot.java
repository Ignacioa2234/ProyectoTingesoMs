package com.tutorial.rack_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "rack_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RackSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Día de la semana (LUNES…DOMINGO)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    // Hora de inicio del bloque (p.ej. 14:00, 14:30, etc.)
    private LocalTime startTime;

    // Hora de fin del bloque (se puede derivar, pero la guardamos para flexibilidad)
    private LocalTime endTime;

    // Código de reserva asociado (si está ocupado), o null si libre
    private String reservationCode;

    // Nombre del cliente o grupo (opcional, para mostrar en rack)
    private String customerName;
}
