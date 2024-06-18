package com.bside405.perfume.project.clova;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PerfumeNameDTO {
    private String name;
    private String eName;

    public PerfumeNameDTO(String name, String englishName) {
        this.name = name;
        this.eName = englishName;
    }
}

