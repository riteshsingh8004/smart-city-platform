package com.smartcity.repository;

import com.smartcity.entity.Report;
import com.smartcity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUser(User user);
    List<Report> findByStatus(Report.Status status);

  /*  List<Report> findByCreatedByEmail(String email);*/

    // ✅ Correct
    List<Report> findByUser_Email(String email);
    List<Report> findByDepartment_Id(Long departmentId);
}
