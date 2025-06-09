// src/main/java/com/tutorial/tarifas_service/entity/Tarifa.java
package com.tutorial.tarifas_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tarifas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_laps", nullable = false)
    private Integer minLaps;

    @Column(name = "max_laps", nullable = false)
    private Integer maxLaps;

    @Column(nullable = false)
    private Integer price;
}
