package com.fitfounder.backend.repository;

import com.fitfounder.backend.domain.BestFit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BestFitRepository extends JpaRepository<BestFit, String> {
    List<BestFit> findByUserId(String userId);
}
