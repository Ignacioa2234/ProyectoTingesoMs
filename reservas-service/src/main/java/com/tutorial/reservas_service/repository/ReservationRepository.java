package com.tutorial.reservas_service.repository;

import com.tutorial.reservas_service.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
