package com.tutorial.rack_service.repository;

import com.tutorial.rack_service.entity.RackSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RackSlotRepository extends JpaRepository<RackSlot, Long> {

    // Obtener todos los slots de un día ordenados
    List<RackSlot> findByDayOrderByStartTime(DayOfWeek day);

    // Buscar un slot concreto por día y hora
    RackSlot findByDayAndStartTime(DayOfWeek day, LocalTime startTime);

    // Obtener bloques ocupados para la semana
    List<RackSlot> findByReservationCodeIsNotNull();
}
