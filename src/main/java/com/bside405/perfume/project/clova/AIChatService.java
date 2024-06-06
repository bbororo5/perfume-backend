package com.bside405.perfume.project.clova;

import reactor.core.publisher.Flux;

public interface AIChatService {
    String explainRecommendedPerfume(Long perfumeId);
    Flux<String> explainRecommendedPerfumeStream(Long perfumeId);

    String prepareRequestJSON(Long perfumeId);
}