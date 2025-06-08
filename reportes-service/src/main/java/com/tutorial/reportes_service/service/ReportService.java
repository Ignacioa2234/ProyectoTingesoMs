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


    public List<ReportEntity> getLapsTimeReport(LocalDateTime start, LocalDateTime end) {
        return reportRepository.reportByLapsOrTime(start, end);
    }

    public List<ReportEntity> getPeopleCountReport(LocalDateTime start, LocalDateTime end) {
        return reportRepository.reportByGroupSize(start, end);
    }

    public List<ReportEntity> getAllReports() {
        return reportRepository.findAll();
    }

    public List<ReportEntity> getReportsByType(String type) {
        return reportRepository.findAllByReportType(type);
    }

    public ReportEntity saveReport(ReportEntity report) {
        report.setReportDate(LocalDateTime.now());
        return reportRepository.save(report);
    }
}
