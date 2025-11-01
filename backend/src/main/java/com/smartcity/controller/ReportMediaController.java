package com.smartcity.controller;

import com.smartcity.entity.ReportMedia;
import com.smartcity.service.ReportMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reports/media")
@RequiredArgsConstructor
public class ReportMediaController {

    private final ReportMediaService reportMediaService;

    @PostMapping("/{reportId}/upload")
    public ResponseEntity<?> upload(@PathVariable Long reportId,
                                    @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(reportMediaService.uploadMedia(reportId, file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<List<ReportMedia>> getMedia(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportMediaService.getMediaByReport(reportId));
    }
}
