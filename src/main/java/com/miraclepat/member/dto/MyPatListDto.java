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

    private List<MyPatDto> content = new ArrayList<>();

    private Boolean hasNext;

    @Builder
    public MyPatListDto(List<Pat> patList){
        this.content = patList.stream()
                .map(MyPatDto::new).collect(Collectors.toList());
        this.hasNext = true;

    }
}
