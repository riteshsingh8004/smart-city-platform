package com.smartcity.dto;

import com.smartcity.entity.Report;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportStatusUpdateRequest {
    private Report.Status status;
}
