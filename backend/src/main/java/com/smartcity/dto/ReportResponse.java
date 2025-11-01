package com.smartcity.dto;

import com.smartcity.entity.Report;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Report.Status status;
    private String departmentName;
    private Double latitude;
    private Double longitude;
    private String locationText;
    private Integer priority;
    private String createdByEmail;
    private LocalDateTime createdAt;
}
