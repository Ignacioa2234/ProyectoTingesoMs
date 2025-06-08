package com.tutorial.reportes_service.controller;

import com.tutorial.reportes_service.entity.ReportEntity;
import com.tutorial.reportes_service.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReportController {
    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReportEntity> getAll() {
        return service.getAllReports();
    }

    @GetMapping("/type/{type}")
    public List<ReportEntity> getByType(@PathVariable String type) {
        return service.getReportsByType(type);
    }

    @PostMapping
    public ResponseEntity<ReportEntity> create(@RequestBody ReportEntity report) {
        ReportEntity saved = service.saveReport(report);
        return ResponseEntity.status(201).body(saved);
    }
}
