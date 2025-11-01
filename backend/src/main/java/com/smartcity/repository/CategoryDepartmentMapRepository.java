package com.smartcity.repository;

import com.smartcity.entity.CategoryDepartmentMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryDepartmentMapRepository extends JpaRepository<CategoryDepartmentMap, Long> {
    Optional<CategoryDepartmentMap> findByCategoryIgnoreCase(String category);
}
