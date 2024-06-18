package com.bside405.perfume.project.clova;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/api/clova")
@RequiredArgsConstructor
public class AIChatController {

    private final AIExplainService aiChatService;

    @GetMapping(value = "/perfume/{id}/explanation/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> explainRecommendedPerfumeStream(@PathVariable Long id) {
        return aiChatService.explainByStream(id);
    }
}
