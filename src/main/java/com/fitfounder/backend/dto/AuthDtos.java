package com.fitfounder.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDtos {
    @Data
    public static class SocialLoginRequest {
        @NotBlank
        private String provider;
        @NotBlank
        private String providerUserId;
        @Email
        @NotBlank
        private String email;
        private String displayName;
        private String userAgent;
        private String deviceName;
        private String ipAddress;
    }

    @Data
    @Builder
    public static class AuthTokens {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
    }

    @Data
    @Builder
    public static class AuthResponse {
        private String userId;
        private AuthTokens tokens;
    }

    @Data
    public static class RefreshRequest {
        @NotNull
        private String refreshToken;
    }
}
