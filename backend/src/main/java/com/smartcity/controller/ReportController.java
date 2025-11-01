package com.smartcity.controller;

import com.smartcity.dto.*;
import com.smartcity.entity.ReportMedia;
import com.smartcity.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReportRequest req, Authentication auth) {
        return ResponseEntity.ok(reportService.createReport(req, auth.getName()));
    }

    @GetMapping("/my")
    public ResponseEntity<?> myReports(Authentication auth) {
        return ResponseEntity.ok(reportService.getMyReports(auth.getName()));
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<?> getByDept(@PathVariable Long deptId) {
        return ResponseEntity.ok(reportService.getReportsByDepartment(deptId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody ReportStatusUpdateRequest req,
                                          Authentication auth) {
        String updatedBy = auth.getName(); // 👈 current user’s email or username
        return ResponseEntity.ok(reportService.updateStatus(id, req, updatedBy));
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<ReportMedia> uploadMedia(@PathVariable Long id, @RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok(reportService.uploadMedia(id, file));
    }
}
