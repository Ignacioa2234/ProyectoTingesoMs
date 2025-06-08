package com.tutorial.reportes_service.service;

import com.tutorial.reportes_service.entity.ReportEntity;
import com.tutorial.reportes_service.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /** Devuelve todos los reportes almacenados. */
    public List<ReportEntity> getAllReports() {
        return reportRepository.findAll();
    }

    /** Filtra los reportes por un tipo concreto (“laps” o “group-size”). */
    public List<ReportEntity> getReportsByType(String type) {
        return reportRepository.findAllByReportType(type);
    }

    /** Guarda un nuevo reporte en la base de datos. */
    public ReportEntity saveReport(ReportEntity report) {
        return reportRepository.save(report);
    }
}
