package com.tutorial.reservas_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "num_persons", nullable = false)
    private Integer numberOfPersons;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @ElementCollection
    @CollectionTable(
            name = "reservation_group_emails",
            joinColumns = @JoinColumn(name = "reservation_id")
    )
    @Column(name = "email")
    private List<String> groupEmails;
}
