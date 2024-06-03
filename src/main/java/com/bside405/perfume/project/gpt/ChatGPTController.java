package com.bside405.perfume.project.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @GetMapping("/perfume/{id}/explanation")
    public ResponseEntity<String> explainRecommendedPerfume(@PathVariable Long id) {
        String explanation = chatGPTService.explainRecommendedPerfume(id);
        return ResponseEntity.ok(explanation);
    }
}
