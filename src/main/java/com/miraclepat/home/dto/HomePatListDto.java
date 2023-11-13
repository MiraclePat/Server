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

    private List<HomePatDto> content = new ArrayList<>();

    private boolean hasNext;


    @Builder
    public HomePatListDto(List<Pat> patList){
        this.content = patList.stream()
                .map(HomePatDto::new).collect(Collectors.toList());
        this.hasNext = true;

    }
}
