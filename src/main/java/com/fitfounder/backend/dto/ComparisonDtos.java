package com.fitfounder.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ComparisonDtos {
    @Data
    public static class ComparisonStartRequest {
        @NotBlank
        private String comparisonId;
        @NotBlank
        private String userId;
        @NotBlank
        private String bestfitId;
        @NotBlank
        private String sizeChartId;
        private String newfitSelectedRowId;
    }

    @Data
    public static class DimensionDifferencePayload {
        @NotBlank
        private String keyCode;
        @NotBlank
        private String unit;
        @NotNull
        private Double bestfitValue;
        @NotNull
        private Double newfitValue;
        @NotNull
        private Double delta;
    }

    @Data
    public static class DifferencesRequest {
        @Valid
        @NotNull
        private List<DimensionDifferencePayload> differences;
    }

    @Data
    public static class ComparisonResponse {
        private String comparisonId;
        private String userId;
        private String bestfitId;
        private String sizeChartId;
        private String newfitSelectedRowId;
        private String status;
        private String decision;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private List<DimensionDifferencePayload> differences;
    }
}
