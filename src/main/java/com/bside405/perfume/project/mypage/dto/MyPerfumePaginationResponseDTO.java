package com.bside405.perfume.project.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MyPerfumePaginationResponseDTO {
    private List<MyPerfumeResponseDTO> content;
    private int totalPages;
    private long totalItems;
}
