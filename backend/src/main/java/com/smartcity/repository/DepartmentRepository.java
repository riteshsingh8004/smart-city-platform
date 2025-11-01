package com.smartcity.repository;

import com.smartcity.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    List<Department> findByCodeStartingWith(String prefix);
    Optional<Department> findByNameIgnoreCase(String name);
}
