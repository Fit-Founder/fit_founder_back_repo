package com.fitfounder.backend.service;

import com.fitfounder.backend.domain.SocialAccount;
import com.fitfounder.backend.domain.User;
import com.fitfounder.backend.dto.AuthDtos;
import com.fitfounder.backend.repository.SocialAccountRepository;
import com.fitfounder.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final SocialAccountRepository socialAccountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AuthDtos.AuthResponse socialLogin(AuthDtos.SocialLoginRequest request) {
        SocialAccount account = socialAccountRepository
                .findByProviderAndProviderUserId(request.getProvider(), request.getProviderUserId())
                .orElseGet(() -> createAccountAndUser(request));

        AuthDtos.AuthTokens tokens = AuthDtos.AuthTokens.builder()
                .accessToken("jwt-access-" + account.getUserId())
                .refreshToken("jwt-refresh-" + account.getUserId())
                .tokenType("bearer")
                .build();

        return AuthDtos.AuthResponse.builder()
                .userId(account.getUserId())
                .tokens(tokens)
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
}
