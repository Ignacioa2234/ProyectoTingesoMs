package com.tutorial.reportes_service.repository;

import com.tutorial.reportes_service.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    List<ReportEntity> findAllByReportType(String reportType);

}
