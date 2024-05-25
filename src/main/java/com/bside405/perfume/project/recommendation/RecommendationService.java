package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.exception.HashtagNotFoundException;
import com.bside405.perfume.project.exception.PerfumeNotFoundException;
import com.bside405.perfume.project.perfume.Hashtag;
import com.bside405.perfume.project.perfume.Perfume;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import com.bside405.perfume.project.perfume.PerfumeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final PerfumeRepository perfumeRepository;
    private final HashtagRepository hashtagRepository;

    public RecommendationResponseDTO recommendPerfume(RecommendationRequestDTO requestDTO) {
        log.debug("Received request to recommend perfume with hashtags: {}", requestDTO.getHashtagList());

        if (requestDTO.getHashtagList().isEmpty()){
            log.warn("Hashtag list is empty, throwing HashtagNotFoundException");
            throw new HashtagNotFoundException("향수 추천을 위해 해시태그가 필요합니다.");
        }

        List<Long> hashtagIds = new ArrayList<>();
        for (String hashtagName : requestDTO.getHashtagList()) {
            Hashtag hashtag = hashtagRepository.findByName(hashtagName);
            if (hashtag == null) {
                log.warn("Hashtag with name '{}' not found, throwing HashtagNotFoundException", hashtagName);
                throw new HashtagNotFoundException(hashtagName + "라는 이름의 해시태그를 찾을 수 없습니다");
            }
            hashtagIds.add(hashtag.getId());
        }

        log.debug("Converted hashtag names to IDs: {}", hashtagIds);

        Pageable pageable = PageRequest.of(0, 6);
        List<Long> perfumeIds = recommendationRepository.findTopPerfumeIdsByHashtagIds(hashtagIds, pageable);

        log.debug("Found top perfume IDs: {}", perfumeIds);

        if (perfumeIds.isEmpty()) {
            log.warn("No perfumes found for given hashtags, throwing PerfumeNotFoundException");
            throw new PerfumeNotFoundException("해시태그와 일치하는 향수를 찾을 수 없습니다.");
        }

        List<Perfume> perfumes = perfumeRepository.findAllById(perfumeIds);

        log.debug("Retrieved perfumes from database: {}", perfumes);

        Perfume mainPerfume = perfumes.get(0);
        PerfumeResponseDTO mainPerfumeDTO = new PerfumeResponseDTO(mainPerfume);

        List<Perfume> subPerfumes = perfumes.subList(1, Math.min(perfumes.size(), 6));
        List<PerfumeResponseDTO> subPerfumeDTOs = new LinkedList<>();

        for (Perfume perfume : subPerfumes) {
            PerfumeResponseDTO subPerfumeDTO = new PerfumeResponseDTO(perfume);
            subPerfumeDTOs.add(subPerfumeDTO);
        }

        RecommendationResponseDTO recommendationResponseDTO = new RecommendationResponseDTO();
        recommendationResponseDTO.setMainPerfume(mainPerfumeDTO);
        recommendationResponseDTO.setSubPerfumes(subPerfumeDTOs);

        log.debug("Created RecommendationResponseDTO with mainPerfume: {}, subPerfumes: {}",
                recommendationResponseDTO.getMainPerfume(), recommendationResponseDTO.getSubPerfumes());

        return recommendationResponseDTO;
    }
}
