package com.bside405.perfume.project.mypage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPerfumeResponseDTO {

    private Long myPerfumeId;
    private String name;
    private String eName;
    private String brand;
    private String imageURL;
}
