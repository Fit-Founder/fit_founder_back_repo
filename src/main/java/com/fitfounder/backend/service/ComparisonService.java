package com.fitfounder.backend.service;

import com.fitfounder.backend.domain.Comparison;
import com.fitfounder.backend.domain.DimensionDifference;
import com.fitfounder.backend.domain.DimensionDifferenceId;
import com.fitfounder.backend.dto.ComparisonDtos;
import com.fitfounder.backend.repository.BestFitRepository;
import com.fitfounder.backend.repository.ComparisonRepository;
import com.fitfounder.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComparisonService {
    private final ComparisonRepository comparisonRepository;
    private final BestFitRepository bestFitRepository;
    private final UserRepository userRepository;

    @Transactional
    public ComparisonDtos.ComparisonResponse start(ComparisonDtos.ComparisonStartRequest request) {
        userRepository.findById(request.getUserId()).orElseThrow(() -> new IllegalArgumentException("user not found"));
        bestFitRepository.findById(request.getBestfitId()).orElseThrow(() -> new IllegalArgumentException("bestfit not found"));

        Comparison comparison = new Comparison();
        comparison.setComparisonId(request.getComparisonId());
        comparison.setUserId(request.getUserId());
        comparison.setBestfitId(request.getBestfitId());
        comparison.setSizeChartId(request.getSizeChartId());
        comparison.setNewfitSelectedRowId(request.getNewfitSelectedRowId());
        comparison.setStatus("running");
        comparisonRepository.save(comparison);
        return toResponse(comparison);
    }

    @Transactional
    public ComparisonDtos.ComparisonResponse saveDifferences(String comparisonId, ComparisonDtos.DifferencesRequest request) {
        Comparison comparison = comparisonRepository.findById(comparisonId)
                .orElseThrow(() -> new IllegalArgumentException("comparison not found"));
        comparison.getDifferences().clear();
        request.getDifferences().forEach(diff -> {
            DimensionDifference entity = new DimensionDifference();
            entity.setId(new DimensionDifferenceId(comparisonId, diff.getKeyCode()));
            entity.setComparison(comparison);
            entity.setUnit(diff.getUnit());
            entity.setBestfitValue(diff.getBestfitValue());
            entity.setNewfitValue(diff.getNewfitValue());
            entity.setDelta(diff.getDelta());
            comparison.getDifferences().add(entity);
        });
        comparison.setStatus("succeeded");
        return toResponse(comparisonRepository.save(comparison));
    }

    public Optional<ComparisonDtos.ComparisonResponse> get(String comparisonId) {
        return comparisonRepository.findById(comparisonId).map(this::toResponse);
    }

    private ComparisonDtos.ComparisonResponse toResponse(Comparison comparison) {
        ComparisonDtos.ComparisonResponse response = new ComparisonDtos.ComparisonResponse();
        response.setComparisonId(comparison.getComparisonId());
        response.setUserId(comparison.getUserId());
        response.setBestfitId(comparison.getBestfitId());
        response.setSizeChartId(comparison.getSizeChartId());
        response.setNewfitSelectedRowId(comparison.getNewfitSelectedRowId());
        response.setStatus(comparison.getStatus());
        response.setDecision(comparison.getDecision());
        response.setCreatedAt(comparison.getCreatedAt());
        response.setUpdatedAt(comparison.getUpdatedAt());
        response.setDifferences(comparison.getDifferences().stream().map(diff -> {
            ComparisonDtos.DimensionDifferencePayload payload = new ComparisonDtos.DimensionDifferencePayload();
            payload.setKeyCode(diff.getId().getKeyCode());
            payload.setUnit(diff.getUnit());
            payload.setBestfitValue(diff.getBestfitValue());
            payload.setNewfitValue(diff.getNewfitValue());
            payload.setDelta(diff.getDelta());
            return payload;
        }).toList());
        return response;
    }
}
