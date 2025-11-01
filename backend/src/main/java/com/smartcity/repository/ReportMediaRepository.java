package com.smartcity.repository;

import com.smartcity.entity.ReportMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportMediaRepository extends JpaRepository<ReportMedia, Long> {
    List<ReportMedia> findByReportId(Long reportId);
}
