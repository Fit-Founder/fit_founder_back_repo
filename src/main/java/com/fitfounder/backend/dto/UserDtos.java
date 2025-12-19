package com.fitfounder.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserDtos {
    @Data
    public static class CreateUserRequest {
        @NotBlank
        private String userId;

        @NotBlank
        @Size(max = 100)
        private String nickname;

        @NotNull
        @Min(100)
        @Max(230)
        private Integer heightCm;

        @NotNull
        @Min(20)
        @Max(250)
        private Integer weightKg;

        @NotBlank
        private String gender;

        private String profileImageAssetId;
    }

    @Data
    public static class UserResponse {
        private String userId;
        private String nickname;
        private Integer heightCm;
        private Integer weightKg;
        private String gender;
        private String profileImageAssetId;
        private boolean seenTutorialFlag;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }
}
