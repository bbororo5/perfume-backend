package com.bside405.perfume.project.clova;

import reactor.core.publisher.Flux;

public interface AIChatService {
    String explain(Long perfumeId);
    Flux<String> explainByStream(Long perfumeId);
    String prepareRequestJSON(Long perfumeId);
}