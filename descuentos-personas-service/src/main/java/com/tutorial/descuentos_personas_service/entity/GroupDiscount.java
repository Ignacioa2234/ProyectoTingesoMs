package com.tutorial.descuentos_personas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "group_discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer minSize;

    private Integer maxSize;

    @Column(nullable = false)
    private Integer discountValue;
}
