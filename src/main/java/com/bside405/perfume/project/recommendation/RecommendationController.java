package com.bside405.perfume.project.recommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/recommend")
    public ResponseEntity<RecommendationResponseDTO> recommendPerfume(@RequestBody RecommendationRequestDTO requestDTO) {
        RecommendationResponseDTO responseDTO = recommendationService.recommendPerfume(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
