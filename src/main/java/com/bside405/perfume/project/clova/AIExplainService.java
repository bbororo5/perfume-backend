package com.bside405.perfume.project.clova;

import reactor.core.publisher.Flux;

public interface AIExplainService {
    Flux<String> explainByStream(Long perfumeId);
}