package com.bside405.perfume.project.perfume;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PerfumeRequestDTO {

    @NotBlank(message = "제품 이름을 작성하세요.")
    private String name;
    @NotBlank(message = "브랜드를 작성하세요.")
    private String brand;
    @NotBlank(message = "사진을 첨부하세요")
    private String imageURL;
}



