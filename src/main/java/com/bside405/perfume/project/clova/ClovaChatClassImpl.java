package com.bside405.perfume.project.clova;

import com.bside405.perfume.project.perfume.PerfumeHashtagRepository;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

@Slf4j
@Service
@Primary
public class ClovaChatClassImpl extends AbstractAIChatService {

    @Value("${clova.api.key}")
    private String clovaApiKey;
    @Value("${clova.gateway.api.key}")
    private String gatewayApiKey;
    @Value("${clova.request.id}")
    private String clovaRequestId;

    private final String CLOVA_API_URL = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-DASH-001";
    private final String CLOVA_API_KEY_HEADER = "X-NCP-CLOVASTUDIO-API-KEY";
    private final String GATEWAY_API_KEY_HEADER = "X-NCP-APIGW-API-KEY";
    private final String CLOVA_STUDIO_REQUEST_ID = "X-NCP-CLOVASTUDIO-REQUEST-ID";

    private final WebClient webClient;

    public ClovaChatClassImpl(PerfumeHashtagRepository perfumeHashtagRepository, PerfumeRepository perfumeRepository,
                              WebClient webClient) {
        super(perfumeHashtagRepository, perfumeRepository);
        this.webClient = webClient;
    }

    @Override
    public Flux<String> explainByStream(Long perfumeId) {
        log.debug("Clova Studio 스트림 요청 작업 시작");
        String requestJson = prepareRequestJSON(perfumeId);

        return webClient.post()
                .uri(CLOVA_API_URL)
                .header(CLOVA_API_KEY_HEADER, clovaApiKey)
                .header(GATEWAY_API_KEY_HEADER, gatewayApiKey)
                .header(CLOVA_STUDIO_REQUEST_ID, clovaRequestId)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .header(HttpHeaders.CONNECTION, "keep-alive")
                .bodyValue(requestJson)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> {
                            log.error("Error response status: {}", response.statusCode());
                            return Mono.error(new RuntimeException("Error response status: " + response.statusCode()));
                        }
                )
                .bodyToFlux(String.class)
                .takeUntil(data -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode jsonNode = mapper.readTree(data);
                        String stopReason = jsonNode.path("stopReason").asText();
                        return "stop_before".equals(stopReason);
                    } catch (JsonProcessingException e) {
                        log.error("Error processing JSON", e);
                        return false;
                    }
                })
                .doOnError(WebClientResponseException.class, e -> {
                    log.error("WebClientResponseException: {}", e.getMessage());
                    // Custom handling for WebClientResponseException
                })
                .doOnError(ConnectException.class, e -> {
                    log.error("ConnectException: {}", e.getMessage());
                    // Custom handling for connection timeout
                })
                .doOnError(SocketTimeoutException.class, e -> {
                    log.error("SocketTimeoutException: {}", e.getMessage());
                    // Custom handling for read or write timeout
                })
                .doOnError(e -> {
                    log.error("Unexpected error: {}", e.getMessage());
                    // Generic error handling
                });
    }

    @Override
    public String prepareRequestJSON(Long perfumeId) {
        PerfumeNameDTO perfumeNameDTO = super.getKoreanAndEnglishNameOfRecommendedPerfume(perfumeId);
        String koreanName = perfumeNameDTO.getName();
        String englishName = perfumeNameDTO.getEName();

        List<String> hashtagNameList = super.getAllHashtagsOfRecommendedPerfume(perfumeId);
        String hashtags = String.join(", ", hashtagNameList);

        String content = String.format("너는 향수 전문가야. 사용자는 자신이 선호하는 키워드를 가지고 외부에서 향수를 추천받았어. 사용자는 너에게 키워드 몇 개를 주면, 그 키워드가 추천된 향수에 왜 어울리는지 설득력 있으면서 오감을 자극하는 설명을 하면돼. 여기에는 문학적 표현을 적극 활용하여 향수가 안겨다주는 인상을 알려줘.\n\n"
                + "또한 향수가 언제, 어디서, 왜 출시 되었는지에 대해 설명해. 특히 사회에 어떤 반향을 일으켰는지 등 사회적 맥락과 함께 향수의 정보를 제시해.\n\n"
                + "그리고 브랜드도 키워드에 포함될텐데, 그 브랜드가 추구하는 향수의 방향도 같이 제시하여, 해당 브랜드를 소비자가 이해할 수 있도록 설명해.\n\n"
                + "키워드를 언급하거나, 키워드 일일이 열거하며 설명하는 답변을 원하지는 않아. 사용자는 한참 전에 키워드를 선택했으며, 자신이 어떤 키워드를 골랐는지 기억하기 어려워.\n\n"
                + "너의 답변은 사용자가 가지는 향수에 대한 궁금증을 해소하며, 추천받은 향수의 구매 독려를 목표로 하고 있어.\n\n"
                + "문단을 나누어 구조적이고 가독성 있는 글을 제시해.\n\n"
                + "추천받은 향수: %s(%s)\n\n"
                + "키워드: %s.", koreanName, englishName, hashtags);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestJson = mapper.createObjectNode();
        ObjectNode message = mapper.createObjectNode();
        message.put("role", "system");
        message.put("content", content);

        requestJson.set("messages", mapper.createArrayNode().add(message));
        requestJson.put("topP", 0.8);
        requestJson.put("topK", 0);
        requestJson.put("maxTokens", 500);
        requestJson.put("temperature", 0.5);
        requestJson.put("repeatPenalty", 10.0);
        requestJson.put("stopBefore", mapper.createArrayNode());
        requestJson.put("includeAiFilters", false);
        requestJson.put("seed", 0);

        try {
            return mapper.writeValueAsString(requestJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Processing Exception", e);
        }
    }
}
