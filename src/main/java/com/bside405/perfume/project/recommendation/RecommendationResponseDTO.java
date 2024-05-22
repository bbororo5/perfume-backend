package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.perfume.PerfumeResponseDTO;
import lombok.Setter;

import java.util.List;

@Setter
public class RecommendationResponseDTO {
    private PerfumeResponseDTO mainPerfume;
    private List<PerfumeResponseDTO> subPerfumes;
}
