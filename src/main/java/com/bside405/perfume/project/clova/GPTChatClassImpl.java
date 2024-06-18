package com.bside405.perfume.project.clova;

import com.bside405.perfume.project.exception.*;
import com.bside405.perfume.project.perfume.PerfumeHashtagRepository;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Service
public class GPTChatClassImpl extends AbstractAIChatService {

    @Value("${openai.api.key}")
    private String apiKey;

    public GPTChatClassImpl(PerfumeHashtagRepository perfumeHashtagRepository, PerfumeRepository perfumeRepository) {
        super(perfumeHashtagRepository, perfumeRepository);
    }

    @Override
    public Flux<String> explainByStream(Long perfumeId) {
        return null;
    }

    @Override
    public String prepareRequestJSON(Long perfumeId) {
        PerfumeNameDTO perfumeNameDTO = super.getKoreanAndEnglishNameOfRecommendedPerfume(perfumeId);

        log.debug("name : {}", perfumeNameDTO.getName());
        String koreanName = perfumeNameDTO.getName();
        log.debug("ename : {}", perfumeNameDTO.getEName());
        String englishName = perfumeNameDTO.getEName();

        log.debug("해시태그들 가져오기 시작");
        List<String> hashtagNameList = super.getAllHashtagsOfRecommendedPerfume(perfumeId);
        log.debug("hashtagNameList : {}", hashtagNameList);
        String hashtags = String.join(", ", hashtagNameList);

        String prompt = String.format("You are Anna, a fragrance expert. The user has been recommended a perfume based on keywords they prefer. When the user provides you with a few keywords, you should offer a persuasive and sensory-stimulating explanation of why the recommended perfume matches those keywords. Use literary expressions to convey the impressions the perfume evokes.\n" +
                "Additionally, explain when, where, and why the perfume was launched, including its impact on society and the social context. Also, discuss the brand's direction in creating fragrances, helping the consumer understand the brand better.\n" +
                "The user doesn't want you to mention or list the keywords specifically in your response. They chose the keywords a while ago and might not remember what they selected.\n" +
                "Your response should satisfy the user's curiosity about the perfume and encourage them to purchase the recommended fragrance.\n\n" +
                "Note: The response must be in Korean and should not exceed 1000 characters.\n\n" +
                "추천받은 향수: %s(%s)\n\n" +
                "키워드: %s", koreanName, englishName, hashtags);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestJson = mapper.createObjectNode();
        ObjectNode messages = mapper.createObjectNode();

        messages.put("role", "system");
        messages.put("content", prompt);

        requestJson.put("model", "pt-3.5-turbo-16k");
        requestJson.put("messages", mapper.createArrayNode().add(messages));

        try {
            return mapper.writeValueAsString(requestJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
