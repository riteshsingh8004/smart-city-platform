package com.smartcity.service;

import com.smartcity.dto.*;
import com.smartcity.entity.Department;
import com.smartcity.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    // ✅ Create Department with auto code generation
    public DepartmentResponse create(DepartmentRequest req) {
        if (departmentRepository.findByName(req.getName()).isPresent()) {
            throw new RuntimeException("Department already exists");
        }

        String generatedCode = generateDepartmentCode(req.getName());

        Department dep = Department.builder()
                .code(generatedCode)
                .name(req.getName())
                .description(req.getDescription())
                .email(req.getEmail())
                .active(true)
                .build();

        Department saved = departmentRepository.save(dep);

        return mapToResponse(saved);
    }

    // ✅ Update Department
    public DepartmentResponse update(Long id, DepartmentRequest req) {
        Department dep = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        dep.setName(req.getName());
        dep.setDescription(req.getDescription());
        dep.setEmail(req.getEmail());
        if (req.getActive() != null) {
            dep.setActive(req.getActive());
        }


        Department updated = departmentRepository.save(dep);
        return mapToResponse(updated);
    }

    // ✅ Get Department by ID
    public DepartmentResponse getById(Long id) {
        Department dep = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return mapToResponse(dep);
    }

    // ✅ Get All Departments
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Soft Delete (toggle active status)
    public void toggleActive(Long id, boolean active) {
        Department dep = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        dep.setActive(active);
        departmentRepository.save(dep);
    }

    private String generateDepartmentCode(String name) {
        String prefix = name.replaceAll("[^A-Za-z]", "").toUpperCase();
        prefix = prefix.length() >= 3 ? prefix.substring(0, 3) : String.format("%-3s", prefix).replace(' ', 'X');

        final String finalPrefix = prefix; // ✅ effectively final for lambda use

        List<Department> existing = departmentRepository.findByCodeStartingWith(finalPrefix);

        Optional<Integer> maxNum = existing.stream()
                .map(d -> d.getCode().replace(finalPrefix, ""))
                .filter(num -> num.matches("\\d+"))
                .map(Integer::parseInt)
                .max(Comparator.naturalOrder());

        int nextNum = maxNum.map(n -> n + 1).orElse(1);
        return String.format("%s%03d", finalPrefix, nextNum);
    }

    // ✅ Utility: Map Entity → DTO
    private DepartmentResponse mapToResponse(Department d) {
        return DepartmentResponse.builder()
                .id(d.getId())
                .code(d.getCode())
                .name(d.getName())
                .description(d.getDescription())
                .email(d.getEmail())
                .active(d.getActive())
                .build();
    }
}
