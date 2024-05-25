package com.bside405.perfume.project.perfume;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeRequestDTO {

    @NotBlank(message = "제품 이름을 작성하세요.")
    private String name;

    @JsonProperty("eName")
    private String eName;

    @NotBlank(message = "브랜드를 작성하세요.")
    private String brand;

    @NotBlank(message = "사진을 첨부하세요")
    private String imageURL;
}



