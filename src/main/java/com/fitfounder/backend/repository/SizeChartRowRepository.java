package com.fitfounder.backend.repository;

import com.fitfounder.backend.domain.SizeChartRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SizeChartRowRepository extends JpaRepository<SizeChartRow, String> {
    List<SizeChartRow> findBySizeChartId(String sizeChartId);
}
