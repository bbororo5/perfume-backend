package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.exception.HashtagNotFoundException;
import com.bside405.perfume.project.exception.PerfumeNotFoundException;
import com.bside405.perfume.project.perfume.Hashtag;
import com.bside405.perfume.project.perfume.Perfume;
import com.bside405.perfume.project.perfume.PerfumeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationResponseDTO recommendPerfume(RecommendationRequestDTO requestDTO) {
        if (requestDTO.getHashtags().isEmpty()){
            throw new HashtagNotFoundException("향수 추천을 위해 해시태그가 필요합니다.");
        }

        List<Hashtag> hashtags = requestDTO.getHashtags();
        Pageable pageable = PageRequest.of(0, 6);
        List<Perfume> perfumes = recommendationRepository.findTopPerfumeByHashtags(hashtags, pageable);

        if (perfumes.isEmpty()) {
            throw new PerfumeNotFoundException("해시태그와 일치하는 향수를 찾을 수 없습니다.");
        }

        Perfume mainPerfume = perfumes.get(0);
        PerfumeResponseDTO mainPerfumeDTO = new PerfumeResponseDTO(mainPerfume);

        List<Perfume> subPerfumes = perfumes.subList(0, Math.min(perfumes.size(), 6));
        List<PerfumeResponseDTO> subPerfumeDTOs = new LinkedList<>();

        for (Perfume perfume : subPerfumes) {
            PerfumeResponseDTO subPerfumeDTO = new PerfumeResponseDTO(perfume);
            subPerfumeDTOs.add(subPerfumeDTO);
        }

        RecommendationResponseDTO recommendationResponseDTO = new RecommendationResponseDTO();
        recommendationResponseDTO.setMainPerfume(mainPerfumeDTO);
        recommendationResponseDTO.setSubPerfumes(subPerfumeDTOs);

        return recommendationResponseDTO;
    }
}
