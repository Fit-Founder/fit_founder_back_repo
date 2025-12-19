package com.fitfounder.backend.controller;

import com.fitfounder.backend.dto.UserDtos;
import com.fitfounder.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDtos.UserResponse> create(@RequestBody @Valid UserDtos.CreateUserRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDtos.UserResponse> get(@PathVariable String userId) {
        return userService.getUser(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
