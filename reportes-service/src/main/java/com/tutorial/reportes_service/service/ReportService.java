package com.tutorial.reportes_service.service;

import com.tutorial.reportes_service.entity.ReportEntity;
import com.tutorial.reportes_service.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<ReportEntity> getAllReports() {
        return reportRepository.findAll();
    }

    public List<ReportEntity> getReportsByType(String type) {
        return reportRepository.findAllByReportType(type);
    }


    public ReportEntity saveReport(ReportEntity report) {
        report.setReportDate(LocalDateTime.now());  // ← aquí seteas la fecha antes de persistir
        return reportRepository.save(report);
    }
}
