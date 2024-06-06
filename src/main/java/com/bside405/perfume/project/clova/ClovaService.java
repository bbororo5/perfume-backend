package com.bside405.perfume.project.clova;

import com.bside405.perfume.project.exception.*;
import com.bside405.perfume.project.perfume.PerfumeHashtagRepository;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaService {

//   @Value("${openai.api.key}")
//   private String apiKey;
    @Value("${clova.api.key}")
    private String clovaApiKey;
    @Value("${clova.gateway.api.key}")
    private String gatewayApiKey;
    private final RestTemplate restTemplate;
    private final PerfumeRepository perfumeRepository;
    private final PerfumeHashtagRepository perfumeHashtagRepository;

    public String explainRecommendedPerfume(Long perfumeId) {
        log.debug("Clova Studio 요청 작업 시작");
        String url = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-DASH-001";

        List<Object[]> perfumeNameObject = getKoreanAndEnglishNameOfRecommendedPerfume(perfumeId);
        String koreanName = (String) perfumeNameObject.get(0)[0];
        String englishName = (String) perfumeNameObject.get(0)[1];

        List<String> hashtagNameList = getAllHashtagsOfRecommendedPerfume(perfumeId);
        String hashtags = String.join(", ", hashtagNameList);

        String content = String.format("너는 향수 전문가야. 사용자는 자신이 선호하는 키워드를 가지고 외부에서 향수를 추천받았어. 사용자는 너에게 키워드 몇 개를 주면, 그 키워드가 추천된 향수에 왜 어울리는지 설득력 있으면서 오감을 자극하는 설명을 하면돼. 여기에는 문학적 표현을 적극 활용하여 향수가 안겨다주는 인상을 알려줘.\n\n"
                + "또한 향수가 언제, 어디서, 왜 출시 되었는지에 대해 설명해. 특히 사회에 어떤 반향을 일으켰는지 등 사회적 맥락과 함께 향수의 정보를 제시해.\n\n"
                + "그리고 브랜드도 키워드에 포함될텐데, 그 브랜드가 추구하는 향수의 방향도 같이 제시하여, 해당 브랜드를 소비자가 이해할 수 있도록 설명해.\n\n"
                + "키워드를 언급하거나, 키워드 일일이 열거하며 설명하는 답변을 원하지는 않아. 사용자는 한참 전에 키워드를 선택했으며, 자신이 어떤 키워드를 골랐는지 기억하기 어려워.\n\n"
                + "너의 답변은 사용자가 가지는 향수에 대한 궁금증을 해소하며, 추천받은 향수의 구매 독려를 목표로 하고 있어.\n\n"
                + "문단을 나누어 구조적이고 가독성 있는 글을 제시해.\n\n"
                + "추천받은 향수: %s(%s)\n\n"
                + "키워드: %s.", koreanName, englishName, hashtags);

        String requestJson = String.format("{\"messages\": [{\"role\": \"system\", \"content\": \"%s\"}], \"topP\": 0.8, \"topK\": 0, \"maxTokens\": 500, \"temperature\": 0.5, \"repeatPenalty\": 10.0, \"stopBefore\": [], \"includeAiFilters\": false, \"seed\": 0}", content);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", clovaApiKey);
        headers.set("X-NCP-APIGW-API-KEY", gatewayApiKey);
        // headers.set("Accept", "text/event-stream"); // 데이터 스트림 옵션

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            log.debug("요청 시작");
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.debug("요청 완료 response: {}", response);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException: StatusCode={}, ResponseBody={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException(e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException: StatusCode={}, ResponseBody={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException(e.getMessage(), e);
        } catch (ResourceAccessException e) {
            log.error("ResourceAccessException: {}", e.getMessage(), e);
            throw new RuntimeException("Resource access error: " + e.getMessage(), e);
        } catch (RestClientException e) {
            log.error("RestClientException: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while communicating with the API: " + e.getMessage(), e);
        }
    }

    private List<String> getAllHashtagsOfRecommendedPerfume(Long perfumeId) {
        List<String> hashtagNameList = perfumeHashtagRepository.findHashtagNamesByPerfumeId(perfumeId);
        if (hashtagNameList.isEmpty()) {
            throw new HashtagNotFoundException("해시태그를 찾을 수 없습니다.");
        }
        return hashtagNameList;
    }

    private List<Object[]> getKoreanAndEnglishNameOfRecommendedPerfume(Long perfumeId) {
        List<Object[]> perfumeNameObject = perfumeRepository.findNameAndENameById(perfumeId);
        log.debug("perfumeNamaeObject : {}", perfumeNameObject);

        if (perfumeNameObject == null) {
            log.debug("perfumeNameObject null 예외 발생");
            throw new PerfumeNotFoundException("향수를 찾을 수 없습니다");
        }

        return perfumeNameObject;
    }
}
