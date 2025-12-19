package com.fitfounder.backend.service;

import com.fitfounder.backend.config.JwtProperties;
import com.fitfounder.backend.domain.SocialAccount;
import com.fitfounder.backend.domain.User;
import com.fitfounder.backend.domain.UserSession;
import com.fitfounder.backend.dto.AuthDtos;
import com.fitfounder.backend.repository.SocialAccountRepository;
import com.fitfounder.backend.repository.UserRepository;
import com.fitfounder.backend.repository.UserSessionRepository;
import com.fitfounder.backend.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final SocialAccountRepository socialAccountRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Transactional
    public AuthDtos.AuthResponse socialLogin(AuthDtos.SocialLoginRequest request) {
        SocialAccount account = socialAccountRepository
                .findByProviderAndProviderUserId(request.getProvider(), request.getProviderUserId())
                .orElseGet(() -> createAccountAndUser(request));

        UserSession session = createSession(account.getUserId(), request.getUserAgent(), request.getDeviceName(), request.getIpAddress());
        AuthDtos.AuthTokens tokens = issueTokens(account.getUserId(), session);

        return AuthDtos.AuthResponse.builder()
                .userId(account.getUserId())
                .tokens(tokens)
                .build();
    }

    @Transactional
    public AuthDtos.AuthResponse refresh(AuthDtos.RefreshRequest request) {
        var claims = jwtService.parseRefreshClaims(request.getRefreshToken());
        String sessionId = (String) claims.get("sid");
        String userId = claims.getSubject();

        UserSession session = userSessionRepository.findBySessionIdAndRevokedAtIsNull(sessionId)
                .filter(s -> s.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new EntityNotFoundException("Session not found or expired"));

        String hashed = hashToken(request.getRefreshToken());
        if (!hashed.equals(session.getRefreshTokenHash()) || !session.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        AuthDtos.AuthTokens tokens = issueTokens(userId, session);

        return AuthDtos.AuthResponse.builder()
                .userId(userId)
                .tokens(tokens)
                .build();
    }

    @Transactional
    public void logout(AuthDtos.RefreshRequest request) {
        String sessionId = jwtService.extractSessionId(request.getRefreshToken());
        if (sessionId == null) {
            return;
        }
        userSessionRepository.findBySessionIdAndRevokedAtIsNull(sessionId).ifPresent(session -> {
            session.setRevokedAt(LocalDateTime.now());
            userSessionRepository.save(session);
        });
    }

    private AuthDtos.AuthTokens issueTokens(String userId, UserSession session) {
        String accessToken = jwtService.generateAccessToken(userId);
        String refreshToken = jwtService.generateRefreshToken(session.getSessionId(), userId);

        session.setRefreshTokenHash(hashToken(refreshToken));
        session.setExpiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenTtlSeconds()));
        userSessionRepository.save(session);

        return AuthDtos.AuthTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("bearer")
                .build();
    }

    private SocialAccount createAccountAndUser(AuthDtos.SocialLoginRequest request) {
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setUserId(userId);
        user.setNickname(request.getDisplayName() != null ? request.getDisplayName() : "user" + userId.substring(0, 6));
        user.setHeightCm(170);
        user.setWeightKg(60);
        user.setGender("male");
        userRepository.save(user);

        SocialAccount account = new SocialAccount();
        account.setSocialAccountId(UUID.randomUUID().toString());
        account.setUserId(userId);
        account.setProvider(request.getProvider());
        account.setProviderUserId(request.getProviderUserId());
        account.setEmail(request.getEmail());
        account.setDisplayName(request.getDisplayName());
        return socialAccountRepository.save(account);
    }

    private UserSession createSession(String userId, String userAgent, String deviceName, String ipAddress) {
        UserSession session = new UserSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUserId(userId);
        session.setUserAgent(userAgent);
        session.setDeviceName(deviceName);
        session.setIpAddress(ipAddress);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenTtlSeconds()));
        return userSessionRepository.save(session);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
