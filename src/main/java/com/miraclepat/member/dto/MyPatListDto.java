package com.miraclepat.member.dto;

import com.miraclepat.pat.entity.Pat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MyPatListDto {

    private List<MyPatDto> myPatDtoList = new ArrayList<>();

    private Long totalPages;

    private Long totalCount;

    @Builder
    public MyPatListDto(List<Pat> patList, Long totalPages, Long totalCount){
        this.myPatDtoList = patList.stream()
                .map(MyPatDto::new).collect(Collectors.toList());
        this.totalPages = totalPages;
        this.totalCount = totalCount;

    }
}
