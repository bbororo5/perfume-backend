package com.bside405.perfume.project.gpt;

import com.bside405.perfume.project.exception.*;
import com.bside405.perfume.project.perfume.PerfumeHashtagRepository;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    @Value("${openai.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;
    private final PerfumeRepository perfumeRepository;
    private final PerfumeHashtagRepository perfumeHashtagRepository;

    public String explainRecommendedPerfume(Long perfumeId) {
        String url = "https://api.openai.com/v1/chat/completions";

        Object[] perfumeNameArray = getKoreanAndEnglishNameOfRecommendedPerfume(perfumeId);
        String koreanName = (String) perfumeNameArray[0];
        String englishName = (String) perfumeNameArray[1];

        List<String> hashtagNameList = getAllHashtagsOfRecommendedPerfume(perfumeId);
        String hashtags = String.join(", ", hashtagNameList);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String prompt = String.format("You are Anna, a fragrance expert. The user has been recommended a perfume based on keywords they prefer. When the user provides you with a few keywords, you should offer a persuasive and sensory-stimulating explanation of why the recommended perfume matches those keywords. Use literary expressions to convey the impressions the perfume evokes.\n" +
                "Additionally, explain when, where, and why the perfume was launched, including its impact on society and the social context. Also, discuss the brand's direction in creating fragrances, helping the consumer understand the brand better.\n" +
                "The user doesn't want you to mention or list the keywords specifically in your response. They chose the keywords a while ago and might not remember what they selected.\n" +
                "Your response should satisfy the user's curiosity about the perfume and encourage them to purchase the recommended fragrance.\n\n" +
                "Note: The response must be in Korean and should not exceed 1000 characters.\n\n" +
                "추천받은 향수: %s(%s)\n" +
                "키워드: %s", koreanName, englishName, hashtags);

        String requestJson = String.format("{\"model\": \"gpt-3.5-turbo-16k\", \"messages\": [{\"role\": \"system\", \"content\": \"%s\"}]}", prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new GPTClientErrorException(e.getMessage(), e, e.getStatusCode());
        } catch (HttpServerErrorException e) {
            throw new GPTServerErrorException(e.getMessage(), e, e.getStatusCode());
        } catch (ResourceAccessException e) {
            // 네트워크 오류 관련 예외 클래스
            throw new GPTResourceAccessException("Resource access error: " + e.getMessage(), e, HttpStatusCode.valueOf(503));
        } catch (RestClientException e) {
            // 앞선 예외의 상위 예외 클래스
            throw new GPTRestClientException("An error occurred while communicating with the API: " + e.getMessage(), e, HttpStatusCode.valueOf(500));
        }
    }

    private List<String> getAllHashtagsOfRecommendedPerfume(Long perfumeId) {
        List<String> hashtagNameList = perfumeHashtagRepository.findHashtagNamesByPerfumeId(perfumeId);
        if (hashtagNameList.isEmpty()) {
            throw new HashtagNotFoundException("해시태그를 찾을 수 없습니다.");
        }
        return hashtagNameList;
    }

    private Object[] getKoreanAndEnglishNameOfRecommendedPerfume(Long perfumeId) {
        Object[] perfumeNameObject = perfumeRepository.findNameAndENameById(perfumeId);
        if (perfumeNameObject.length == 0) {
            throw new PerfumeNotFoundException("향수를 찾을 수 없습니다");
        }
        return perfumeNameObject;
    }
}
