package com.tutorial.reportes_service.repository;

import com.tutorial.reportes_service.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    @Query("""
        SELECT new com.tutorial.reportes_service.entity.ReportEntity(
            'laps',
            CAST(FUNCTION('MONTHNAME', v.reservationDateTime) AS string),
            CAST(v.maxLapsOrTime AS string),
            SUM(v.totalIncome),
            COUNT(v)
        )
        FROM VoucherEntity v
        WHERE v.issueDate BETWEEN :start AND :end
        GROUP BY
            FUNCTION('YEAR', v.reservationDate),
            FUNCTION('MONTH', v.reportDate),
            CAST(FUNCTION('MONTHNAME', v.reservationDate) AS string),
            CAST(v.maxLapsOrTime AS string)
        ORDER BY
            FUNCTION('YEAR', v.reservationDate),
            FUNCTION('MONTH', v.reservationDate)
    """)
    List<ReportEntity> reportByLapsOrTime(
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end
    );

    @Query("""
        SELECT new com.tutorial.reportes_service.entity.ReportEntity(
            'group-size',
            CAST(FUNCTION('MONTHNAME', v.reservationDateTime) AS string),
            CASE
              WHEN v.peopleCount BETWEEN 1 AND 2 THEN '1-2 personas'
              WHEN v.peopleCount BETWEEN 3 AND 5 THEN '3-5 personas'
              WHEN v.peopleCount BETWEEN 6 AND 10 THEN '6-10 personas'
              ELSE '11+ personas'
            END,
            SUM(v.totalIncome),
            COUNT(v)
        )
        FROM VoucherEntity v
        WHERE v.issueDate BETWEEN :start AND :end
        GROUP BY
            FUNCTION('YEAR', v.reservationDate),
            FUNCTION('MONTH', v.reservationDate),
            CAST(FUNCTION('MONTHNAME', v.reservationDate) AS string),
            CASE
              WHEN v.peopleCount BETWEEN 1 AND 2 THEN '1-2 personas'
              WHEN v.peopleCount BETWEEN 3 AND 5 THEN '3-5 personas'
              WHEN v.peopleCount BETWEEN 6 AND 10 THEN '6-10 personas'
              ELSE '11+ personas'
            END
        ORDER BY
            FUNCTION('YEAR', v.reservationDate),
            FUNCTION('MONTH', v.reservationDate)
    """)
    List<ReportEntity> reportByGroupSize(
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end
    );

    List<ReportEntity> findAllByReportType(String reportType);
}
