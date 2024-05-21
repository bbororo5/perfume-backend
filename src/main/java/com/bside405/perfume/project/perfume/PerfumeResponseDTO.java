package com.bside405.perfume.project.perfume;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeResponseDTO {
    private Long id;
    private String name;
    private String brand;
    private String imageURL;
}
