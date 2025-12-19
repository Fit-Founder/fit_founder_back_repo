package com.fitfounder.backend.repository;

import com.fitfounder.backend.domain.Comparison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComparisonRepository extends JpaRepository<Comparison, String> {
}
