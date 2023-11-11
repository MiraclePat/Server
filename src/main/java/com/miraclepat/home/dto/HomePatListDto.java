package com.miraclepat.home.dto;

import com.miraclepat.pat.entity.Pat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class HomePatListDto {

    private List<HomePatDto> homePatDtoList = new ArrayList<>();

    private Long totalPages;

    private Long totalCount;

    @Builder
    public HomePatListDto(List<Pat> patList, Long totalPages, Long totalCount){
        this.homePatDtoList = patList.stream()
                .map(HomePatDto::new).collect(Collectors.toList());
        this.totalPages = totalPages;
        this.totalCount = totalCount;

    }
}
