package com.fitfounder.backend.service;

import com.fitfounder.backend.domain.BestFit;
import com.fitfounder.backend.domain.SizeChart;
import com.fitfounder.backend.domain.SizeChartRow;
import com.fitfounder.backend.dto.BestFitDtos;
import com.fitfounder.backend.repository.BestFitRepository;
import com.fitfounder.backend.repository.SizeChartRepository;
import com.fitfounder.backend.repository.SizeChartRowRepository;
import com.fitfounder.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BestFitService {
    private final BestFitRepository bestFitRepository;
    private final SizeChartRepository sizeChartRepository;
    private final SizeChartRowRepository sizeChartRowRepository;
    private final UserRepository userRepository;

    @Transactional
    public BestFitDtos.BestFitResponse create(BestFitDtos.CreateBestFitRequest request) {
        userRepository.findById(request.getUserId()).orElseThrow(() -> new IllegalArgumentException("user not found"));
        ensureSizeChartAndRow(request);

        if (Boolean.TRUE.equals(request.getFavorite())) {
            bestFitRepository.findByUserId(request.getUserId()).forEach(existing -> {
                if (existing.isFavorite()) {
                    existing.setFavorite(false);
                    bestFitRepository.save(existing);
                }
            });
        }

        BestFit bestFit = new BestFit();
        bestFit.setBestfitId(request.getBestfitId());
        bestFit.setUserId(request.getUserId());
        bestFit.setSizeChartId(request.getSizeChartId());
        bestFit.setRowId(request.getRowId());
        bestFit.setProductName(request.getProductName());
        bestFit.setMemo(request.getMemo());
        bestFit.setFavorite(Boolean.TRUE.equals(request.getFavorite()));
        bestFit.setPantFitType(request.getPantFitType());
        bestFitRepository.save(bestFit);
        return toResponse(bestFit);
    }

    public List<BestFitDtos.BestFitResponse> listByUser(String userId) {
        return bestFitRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    private void ensureSizeChartAndRow(BestFitDtos.CreateBestFitRequest request) {
        sizeChartRepository.findById(request.getSizeChartId()).orElseGet(() -> {
            SizeChart chart = new SizeChart();
            chart.setSizeChartId(request.getSizeChartId());
            chart.setGarmentType("pants");
            chart.setUnit("cm");
            chart.setSource("manual");
            return sizeChartRepository.save(chart);
        });

        sizeChartRowRepository.findById(request.getRowId()).orElseGet(() -> {
            SizeChartRow row = new SizeChartRow();
            row.setRowId(request.getRowId());
            row.setSizeChartId(request.getSizeChartId());
            row.setSizeLabel("free");
            row.setOrdinal((short) 1);
            return sizeChartRowRepository.save(row);
        });
    }

    private BestFitDtos.BestFitResponse toResponse(BestFit bestFit) {
        BestFitDtos.BestFitResponse response = new BestFitDtos.BestFitResponse();
        response.setBestfitId(bestFit.getBestfitId());
        response.setUserId(bestFit.getUserId());
        response.setSizeChartId(bestFit.getSizeChartId());
        response.setRowId(bestFit.getRowId());
        response.setProductName(bestFit.getProductName());
        response.setMemo(bestFit.getMemo());
        response.setFavorite(bestFit.isFavorite());
        response.setPantFitType(bestFit.getPantFitType());
        response.setCreatedAt(bestFit.getCreatedAt());
        response.setUpdatedAt(bestFit.getUpdatedAt());
        return response;
    }
}
