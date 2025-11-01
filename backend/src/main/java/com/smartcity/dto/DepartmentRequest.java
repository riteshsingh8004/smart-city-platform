package com.smartcity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequest {
    private String name;
    private String description;
    private String email;
    private Boolean active; // ✅ Optional: Used for update or manual activation/deactivation

}
