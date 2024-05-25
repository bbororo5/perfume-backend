package com.bside405.perfume.project.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/recommend")
    public ResponseEntity<RecommendationResponseDTO> recommendPerfume(@RequestBody RecommendationRequestDTO requestDTO) {
        log.debug("Received recommendation request with hashtags: {}", requestDTO.getHashtagList());
        RecommendationResponseDTO responseDTO = recommendationService.recommendPerfume(requestDTO);
        log.debug("Sending response: {}", responseDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
