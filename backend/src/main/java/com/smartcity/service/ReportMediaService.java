package com.smartcity.service;

import com.smartcity.entity.*;
import com.smartcity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportMediaService {

    private final ReportRepository reportRepository;
    private final ReportMediaRepository reportMediaRepository;

    private final String uploadDir = "uploads/reports/";

    public ReportMedia uploadMedia(Long reportId, MultipartFile file) throws IOException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String storagePath = uploadDir + filename;
        file.transferTo(new File(storagePath));

        ReportMedia media = ReportMedia.builder()
                .report(report)
                .storagePath(storagePath)
                .mimeType(file.getContentType())
                .build();

        return reportMediaRepository.save(media);
    }

    public List<ReportMedia> getMediaByReport(Long reportId) {
        return reportMediaRepository.findByReportId(reportId);
    }
}
