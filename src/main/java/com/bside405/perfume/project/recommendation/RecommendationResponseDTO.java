package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.perfume.PerfumeResponseDTO;
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
