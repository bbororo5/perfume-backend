package com.bside405.perfume.project.mypage.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExistsCheckResponseDTO {
    private Long id;
    private Boolean exists;
}
