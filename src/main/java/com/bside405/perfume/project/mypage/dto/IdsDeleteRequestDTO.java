package com.bside405.perfume.project.mypage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IdsDeleteRequestDTO {

    @JsonProperty("ids")
    private List<Long> ids;
}
