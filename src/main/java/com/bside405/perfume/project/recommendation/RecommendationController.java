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
        log.debug("컨트롤러 레이어 시작");
        log.debug("사용자로부터 받은 해시태그 목록들: {}", requestDTO.getHashtagList());
        RecommendationResponseDTO responseDTO = recommendationService.recommendPerfume(requestDTO);
        log.debug("응답DTO: {}", responseDTO);
        log.debug("컨트롤러 레이어 종료");
        return ResponseEntity.ok(responseDTO);
    }
}
