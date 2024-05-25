package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.perfume.Hashtag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendationRequestDTO {
    @JsonProperty("hashtagList")
    private List<String> hashtagList;
}
