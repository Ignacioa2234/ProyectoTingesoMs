package com.tutorial.reservas_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reservationCode;

    private LocalDateTime reservationDate;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer vuelTime;


    @ElementCollection
    @CollectionTable(
            name = "reserva_emails",
            joinColumns = @JoinColumn(name = "reserva_id")
    )
    @Column(name = "email")
    private List<String> groupEmails;

    private String integrantesNombres;

    private Integer numberOfPersons;

    @Column(columnDefinition = "TEXT")
    private String integrantesTarifaBase;
    @Column(columnDefinition = "TEXT")
    private String integrantesDescGrupo;
    @Column(columnDefinition = "TEXT")
    private String integrantesDescFrecuente;
    @Column(columnDefinition = "TEXT")
    private String integrantesDescCumple;
    @Column(columnDefinition = "TEXT")
    private String integrantesNeto;
    @Column(columnDefinition = "TEXT")
    private String integrantesIva;
    @Column(columnDefinition = "TEXT")
    private String integrantesTotal;

    private Double totalNet;
    private Double totalIva;
    private Double totalAmount;

    private Double totalPrice;
}
