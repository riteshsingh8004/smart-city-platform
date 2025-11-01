package com.smartcity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    private String title;
    private String description;
    private String category;
    private Double latitude;
    private Double longitude;
    private String locationText;
    private Integer priority; // optional, default 3
}
