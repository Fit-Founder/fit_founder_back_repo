package com.fitfounder.backend.controller;

import com.fitfounder.backend.dto.BestFitDtos;
import com.fitfounder.backend.service.BestFitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bestfits")
@RequiredArgsConstructor
public class BestFitController {
    private final BestFitService bestFitService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid BestFitDtos.CreateBestFitRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(bestFitService.create(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam("user_id") String userId) {
        return ResponseEntity.ok(bestFitService.listByUser(userId));
    }
}
