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
    private final RestTemplate restTemplate;

    public GPTChatClassImpl(PerfumeHashtagRepository perfumeHashtagRepository, PerfumeRepository perfumeRepository, RestTemplate restTemplate) {
        super(perfumeHashtagRepository, perfumeRepository);
        this.restTemplate = restTemplate;
    }

    public String explain(Long perfumeId) {
        log.debug("gpt 요청 작업 시작");
        String url = "https://api.openai.com/v1/chat/completions";

        //헤더
        log.debug("헤더 셋팅 작업 시작");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        log.debug("headers : {}", headers);

        //바디
        String requestJson = prepareRequestJSON(perfumeId);

        //엔티티
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        log.debug("httpEntity: {}", entity);

        try {
            log.debug("요청 시작");
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.debug("요청 완료 reponse: {}", response);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.debug("예외1");
            throw new GPTClientErrorException(e.getMessage(), e, e.getStatusCode());
        } catch (HttpServerErrorException e) {
            log.debug("예외2");
            throw new GPTServerErrorException(e.getMessage(), e, e.getStatusCode());
        } catch (ResourceAccessException e) {
            log.debug("예외3");
            // 네트워크 오류 관련 예외 클래스
            throw new GPTResourceAccessException("Resource access error: " + e.getMessage(), e, HttpStatusCode.valueOf(503));
        } catch (RestClientException e) {
            log.debug("예외4");
            // 앞선 예외의 상위 예외 클래스
            throw new GPTRestClientException("An error occurred while communicating with the API: " + e.getMessage(), e, HttpStatusCode.valueOf(500));
        }
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
