package com.fitfounder.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BestFitDtos {
    @Data
    public static class CreateBestFitRequest {
        @NotBlank
        private String bestfitId;
        @NotBlank
        private String userId;
        @NotBlank
        private String sizeChartId;
        @NotBlank
        private String rowId;

        @NotBlank
        @Size(max = 20)
        private String productName;

        @Size(max = 100)
        private String memo;

        @NotNull
        private Boolean favorite = false;

        private String pantFitType;
    }

    @Data
    public static class BestFitResponse {
        private String bestfitId;
        private String userId;
        private String sizeChartId;
        private String rowId;
        private String productName;
        private String memo;
        private boolean favorite;
        private String pantFitType;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }
}
