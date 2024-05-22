package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.perfume.Hashtag;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendationRequestDTO {
    private List<String> hashtagList;
}
