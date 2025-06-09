package com.tutorial.tarifas_especiales_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "special_day_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialDayRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private LocalDate date;


    @Column(nullable = false)
    private Integer ratePct;
}
