package com.bside405.perfume.project.mypage;

import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
public class MyPerfumePaginationResponseDTO {
    private List<MyPerfumeResponseDTO> content;
    private int currentPage;
    private int totalPages;
    private long totalItems;

}
