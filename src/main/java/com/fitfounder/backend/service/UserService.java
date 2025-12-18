package com.fitfounder.backend.service;

import com.fitfounder.backend.domain.User;
import com.fitfounder.backend.dto.UserDtos;
import com.fitfounder.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDtos.UserResponse createUser(UserDtos.CreateUserRequest request) {
        if (userRepository.existsById(request.getUserId())) {
            throw new IllegalStateException("user already exists");
        }
        User user = new User();
        user.setUserId(request.getUserId());
        user.setNickname(request.getNickname());
        user.setHeightCm(request.getHeightCm());
        user.setWeightKg(request.getWeightKg());
        user.setGender(request.getGender());
        user.setProfileImageAssetId(request.getProfileImageAssetId());
        userRepository.save(user);
        return toResponse(user);
    }

    public Optional<UserDtos.UserResponse> getUser(String userId) {
        return userRepository.findById(userId).map(this::toResponse);
    }

    private UserDtos.UserResponse toResponse(User user) {
        UserDtos.UserResponse response = new UserDtos.UserResponse();
        response.setUserId(user.getUserId());
        response.setNickname(user.getNickname());
        response.setHeightCm(user.getHeightCm());
        response.setWeightKg(user.getWeightKg());
        response.setGender(user.getGender());
        response.setProfileImageAssetId(user.getProfileImageAssetId());
        response.setSeenTutorialFlag(user.isSeenTutorialFlag());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
