package com.bside405.perfume.project.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IdsDeleteRequestDTO {

    private List<Long> ids;
}
