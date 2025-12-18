package com.fitfounder.backend.controller;

import com.fitfounder.backend.dto.AuthDtos;
import com.fitfounder.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/social-login")
    public ResponseEntity<AuthDtos.AuthResponse> socialLogin(@RequestBody @Valid AuthDtos.SocialLoginRequest request) {
        return ResponseEntity.ok(authService.socialLogin(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthDtos.AuthResponse> refresh(@RequestBody @Valid AuthDtos.RefreshRequest request) {
        // Placeholder: return new tokens using refresh token
        AuthDtos.AuthTokens tokens = AuthDtos.AuthTokens.builder()
                .accessToken("jwt-access-refreshed")
                .refreshToken(request.getRefreshToken())
                .tokenType("bearer")
                .build();
        return ResponseEntity.ok(AuthDtos.AuthResponse.builder().userId("unknown").tokens(tokens).build());
    }
}
