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
        log.debug("향수 추천 시작");
        log.debug("사용자에게 받은 해시태그들: {}", requestDTO.getHashtagList());

        validateHashtagList(requestDTO.getHashtagList());

        List<Long> hashtagIds = convertHashtagNamesToIds(requestDTO.getHashtagList());
        log.debug("해시태그 이름들 IDs로 변환: {}", hashtagIds);

        List<Perfume> perfumes = findRecommendedPerfumes(hashtagIds);
        log.debug("데이터베이스에서 perfumes 정보 가져오기: {}", perfumes);
        log.debug("향수 추천 종료");
        return buildRecommendationResponse(perfumes);
    }

    private void validateHashtagList(List<String> hashtagList) {
        if (hashtagList.isEmpty()) {
            log.warn("hashtagList가 비어있습니다.");
            throw new HashtagNotFoundException("향수 추천을 위해 해시태그가 필요합니다.");
        }
    }

    private List<Long> convertHashtagNamesToIds(List<String> hashtagNames) {
        List<Long> hashtagIds = new ArrayList<>();
        for (String hashtagName : hashtagNames) {
            Hashtag hashtag = hashtagRepository.findByName(hashtagName);
            if (hashtag == null) {
                log.warn("해시태그 이름 '{}'을 데이터베이스에서 찾을 수 없습니다.", hashtagName);
                throw new HashtagNotFoundException(hashtagName+ " 라는 이름의 해시태그를 찾을 수 없습니다.");
            }
            hashtagIds.add(hashtag.getId());
        }
        return hashtagIds;
    }

    private List<Perfume> findRecommendedPerfumes(List<Long> hashtagIds) {
        Pageable pageable = PageRequest.of(0, 6);
        List<Long> perfumeIds = recommendationRepository.findPerfumeIdsByHashtagIdsOrderedByMatchCount(hashtagIds, pageable);
        log.debug("가장 해시태그가 많이 겹치는 향수 {}", perfumeIds);

        if (perfumeIds.isEmpty()) {
            log.warn("해시태그로부터 향수를 찾을 수 없습니다.");
            throw new PerfumeNotFoundException("해시태그와 일치하는 향수를 찾을 수 없습니다.");
        }

        return perfumeRepository.findAllById(perfumeIds);
    }

    private RecommendationResponseDTO buildRecommendationResponse(List<Perfume> perfumes) {
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

        log.debug("응답DTO -> mainPerfume: {}, subPerfumes: {}",
                recommendationResponseDTO.getMainPerfume(), recommendationResponseDTO.getSubPerfumes());

        return recommendationResponseDTO;
    }
}