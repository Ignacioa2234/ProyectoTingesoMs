package com.tutorial.rack_service.service;

import com.tutorial.rack_service.entity.RackSlot;
import com.tutorial.rack_service.repository.RackSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RackService {

    private final RackSlotRepository repository;

    /**
     * Inicializa el rack para una semana dada, creando slots vacíos
     * según el horario de atención.
     */
    public void initializeWeeklyRack() {
        // Ejemplo horario: Lunes-Viernes 14:00-22:00, Sáb-Dom 10:00-22:00
        for (DayOfWeek day : DayOfWeek.values()) {
            LocalTime start = (day.getValue() <= 5) ? LocalTime.of(14, 0) : LocalTime.of(10, 0);
            LocalTime end   = LocalTime.of(22, 0);

            LocalTime t = start;
            while (t.isBefore(end)) {
                LocalTime next = t.plusMinutes(30);
                if (repository.findByDayAndStartTime(day, t) == null) {
                    RackSlot slot = RackSlot.builder()
                            .day(day)
                            .startTime(t)
                            .endTime(next)
                            .reservationCode(null)
                            .customerName(null)
                            .build();
                    repository.save(slot);
                }
                t = next;
            }
        }
    }

    /** Obtiene el rack completo de un día */
    public List<RackSlot> getDayRack(DayOfWeek day) {
        return repository.findByDayOrderByStartTime(day);
    }

    /** Marca un slot como ocupado */
    public RackSlot occupySlot(DayOfWeek day, LocalTime startTime, String reservationCode, String customerName) {
        RackSlot slot = repository.findByDayAndStartTime(day, startTime);
        if (slot == null) {
            throw new RuntimeException("Bloque no encontrado: " + day + " " + startTime);
        }
        slot.setReservationCode(reservationCode);
        slot.setCustomerName(customerName);
        return repository.save(slot);
    }

    /** Libera un slot (p.ej. al cancelar) */
    public RackSlot freeSlot(DayOfWeek day, LocalTime startTime) {
        RackSlot slot = repository.findByDayAndStartTime(day, startTime);
        if (slot == null) {
            throw new RuntimeException("Bloque no encontrado: " + day + " " + startTime);
        }
        slot.setReservationCode(null);
        slot.setCustomerName(null);
        return repository.save(slot);
    }

    /** Obtiene todos los slots ocupados de la semana */
    public List<RackSlot> getOccupiedSlots() {
        return repository.findByReservationCodeIsNotNull();
    }
}
