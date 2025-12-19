package com.fitfounder.backend.controller;

import com.fitfounder.backend.dto.ComparisonDtos;
import com.fitfounder.backend.service.ComparisonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comparisons")
@RequiredArgsConstructor
public class ComparisonController {
    private final ComparisonService comparisonService;

    @PostMapping
    public ResponseEntity<?> start(@RequestBody @Valid ComparisonDtos.ComparisonStartRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(comparisonService.start(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{comparisonId}/differences")
    public ResponseEntity<?> saveDifferences(@PathVariable String comparisonId,
                                             @RequestBody @Valid ComparisonDtos.DifferencesRequest request) {
        try {
            return ResponseEntity.ok(comparisonService.saveDifferences(comparisonId, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{comparisonId}")
    public ResponseEntity<?> get(@PathVariable String comparisonId) {
        return comparisonService.get(comparisonId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
