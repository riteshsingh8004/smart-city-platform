package com.smartcity.service;

import com.smartcity.dto.*;
import com.smartcity.entity.*;
import com.smartcity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ReportMediaRepository mediaRepo;
    private final CategoryDepartmentMapRepository categoryDepartmentMapRepository;

    // ✅ Find department using category_department_map
    private Department findDepartmentForCategory(String category) {
        return categoryDepartmentMapRepository.findByCategoryIgnoreCase(category)
                .map(CategoryDepartmentMap::getDepartment)
                .orElse(null);
    }

    // ✅ Create new report
    public ReportResponse createReport(ReportRequest req, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Department dep = findDepartmentForCategory(req.getCategory());
        System.out.println("Mapped Department for category '" + req.getCategory() + "': " + (dep != null ? dep.getName() : "None"));

        Report report = Report.builder()
                .user(user)
                .title(req.getTitle())
                .description(req.getDescription())
                .category(req.getCategory())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .locationText(req.getLocationText())
                .priority(req.getPriority() != null ? req.getPriority() : 3)
                .department(dep)  // ✅ properly set here
                .status(Report.Status.OPEN)
                .build();

        Report saved = reportRepository.save(report);
        return mapToResponse(saved);
    }

    // ✅ Get reports of a user
    public List<ReportResponse> getMyReports(String email) {
        return reportRepository.findByUser_Email(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get reports for department dashboard
    public List<ReportResponse> getReportsByDepartment(Long departmentId) {
        return reportRepository.findByDepartment_Id(departmentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

   /* // ✅ Update report status (by officer)
    public ReportResponse updateStatus(Long id, ReportStatusUpdateRequest req) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(req.getStatus());
        return mapToResponse(reportRepository.save(report));
    }*/

    public ReportResponse updateStatus(Long reportId, ReportStatusUpdateRequest req, String officerEmail) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // Optional: validate officer belongs to the same department
        User officer = userRepository.findByEmail(officerEmail)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        if (officer.getDepartment() == null ||
                !officer.getDepartment().getId().equals(report.getDepartment().getId())) {
            throw new RuntimeException("Officer not authorized for this department’s report");
        }

        report.setStatus(req.getStatus());
        Report updated = reportRepository.save(report);

        return mapToResponse(updated);
    }


    // ✅ Convert entity to DTO
    private ReportResponse mapToResponse(Report r) {
        return ReportResponse.builder()
                .id(r.getId())
                .title(r.getTitle())
                .description(r.getDescription())
                .category(r.getCategory())
                .status(r.getStatus())
                .departmentName(r.getDepartment() != null ? r.getDepartment().getName() : null)
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .locationText(r.getLocationText())
                .priority(r.getPriority())
                .createdByEmail(r.getUser().getEmail())
                .createdAt(r.getCreatedAt())
                .build();
    }

    // ✅ Upload media to report
    public ReportMedia uploadMedia(Long reportId, MultipartFile file) throws Exception {
        Report report = reportRepository.findById(reportId).orElseThrow();

        String storagePath = "/uploads/" + file.getOriginalFilename();
        file.transferTo(new java.io.File(storagePath));

        ReportMedia media = ReportMedia.builder()
                .report(report)
                .storagePath(storagePath)
                .mimeType(file.getContentType())
                .build();

        return mediaRepo.save(media);
    }


    public ReportResponse getReportById(Long id) {
        Report r = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return mapToResponse(r);
    }
}
