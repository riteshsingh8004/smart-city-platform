package com.smartcity.controller;

import com.smartcity.dto.DepartmentRequest;
import com.smartcity.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // 🔒 Only ADMIN can create departments
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DepartmentRequest req) {
        return ResponseEntity.ok(departmentService.create(req));
    }

    // 👀 Anyone logged in can view departments
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(departmentService.getAll());
    }
}
