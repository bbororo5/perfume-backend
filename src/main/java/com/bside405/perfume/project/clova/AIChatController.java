package com.bside405.perfume.project.clova;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/api/clova")
@RequiredArgsConstructor
public class AIChatController {

    private final AIChatService aiChatService;

    @GetMapping("/perfume/{id}/explanation")
    public ResponseEntity<String> explainRecommendedPerfume(@PathVariable Long id) {
        String explanation = aiChatService.explainRecommendedPerfume(id);
        return ResponseEntity.ok(explanation);
    }

    @GetMapping("/perfume/{id}/explanation/stream")
    public Flux<String> explainRecommendedPerfumeStream(@PathVariable Long id) {
        return aiChatService.explainRecommendedPerfumeStream(id);
    }
}
