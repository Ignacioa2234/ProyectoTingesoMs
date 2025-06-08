package com.tutorial.reportes_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "month_name", nullable = false)
    private String monthName;

    @Column(name = "report_date", nullable = false)
    private LocalDateTime reportDate;

    @Column(name = "aggregation_key", nullable = false)
    private String aggregationKey;

    @Column(name = "total_income", nullable = false)
    private BigDecimal totalIncome;

    @Column(name = "reservation_count", nullable = false)
    private Long reservationCount;

    public ReportEntity(String reportType,
                        String monthName,
                        String aggregationKey,
                        BigDecimal totalIncome,
                        Long reservationCount) {
        this.reportType     = reportType;
        this.monthName      = monthName;
        this.reportDate     = LocalDateTime.now();
        this.aggregationKey = aggregationKey;
        this.totalIncome    = totalIncome.setScale(0, RoundingMode.HALF_UP);
        this.reservationCount = reservationCount;
    }

    public ReportEntity(String reportType,
                        String monthName,
                        String aggregationKey,
                        Double totalIncome,
                        Long reservationCount) {
        this(
                reportType,
                monthName,
                aggregationKey,
                totalIncome != null
                        ? BigDecimal.valueOf(totalIncome).setScale(0, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO,
                reservationCount
        );
    }
}
