package com.bside405.perfume.project.clova;

import com.bside405.perfume.project.exception.HashtagNotFoundException;
import com.bside405.perfume.project.exception.PerfumeNotFoundException;
import com.bside405.perfume.project.perfume.PerfumeHashtagRepository;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public abstract class AbstractAIChatService implements AIChatService {

    protected final PerfumeHashtagRepository perfumeHashtagRepository;
    protected final PerfumeRepository perfumeRepository;

    @Autowired
    public AbstractAIChatService(PerfumeHashtagRepository perfumeHashtagRepository, PerfumeRepository perfumeRepository) {
        this.perfumeHashtagRepository = perfumeHashtagRepository;
        this.perfumeRepository = perfumeRepository;
    }

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
