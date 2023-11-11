package com.miraclepat.pat.dto;

import com.miraclepat.pat.entity.Pat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PatListDto {

    private List<PatDto> patDtoList = new ArrayList<>();

    private Long totalPages;

    private Long totalCount;

    @Builder
    public PatListDto(List<Pat> patList, Long totalPages, Long totalCount){
        this.patDtoList = patList.stream()
                .map(PatDto::new).collect(Collectors.toList());
        this.totalPages = totalPages;
        this.totalCount = totalCount;

    }
}
