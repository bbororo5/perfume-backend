package com.bside405.perfume.project.clova;

import com.bside405.perfume.project.exception.HashtagNotFoundException;
import com.bside405.perfume.project.exception.PerfumeNotFoundException;
import com.bside405.perfume.project.perfume.PerfumeHashtagRepository;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAIChatService implements AIChatService {

    private PerfumeHashtagRepository perfumeHashtagRepository;
    private PerfumeRepository perfumeRepository;

    protected List<String> getAllHashtagsOfRecommendedPerfume(Long perfumeId) {
        List<String> hashtagNameList = perfumeHashtagRepository.findHashtagNamesByPerfumeId(perfumeId);
        if (hashtagNameList.isEmpty()) {
            throw new HashtagNotFoundException("해시태그를 찾을 수 없습니다.");
        }
        return hashtagNameList;
    }

    protected PerfumeNameDTO getKoreanAndEnglishNameOfRecommendedPerfume(Long perfumeId) {
        PerfumeNameDTO perfumeNameDTO = perfumeRepository.findNameAndENameById(perfumeId);
        log.debug("perfumeNamaDTO : {}", perfumeNameDTO);

        if (perfumeNameDTO == null) {
            log.error("perfumeNameDTO null 예외 발생");
            throw new PerfumeNotFoundException("향수를 찾을 수 없습니다");
        }

        return perfumeNameDTO;
    }
}
