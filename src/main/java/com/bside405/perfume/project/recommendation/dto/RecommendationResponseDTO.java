package com.bside405.perfume.project.recommendation.dto;

import com.bside405.perfume.project.perfume.dto.PerfumeResponseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RecommendationResponseDTO {
    private PerfumeResponseDTO mainPerfume;
    private List<PerfumeResponseDTO> subPerfumes;
}
