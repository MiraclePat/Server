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

    private List<PatDto> content = new ArrayList<>();

    private Boolean hasNext;

    @Builder
    public PatListDto(List<Pat> patList){
        this.content = patList.stream()
                .map(PatDto::new).collect(Collectors.toList());
        this.hasNext = true;

    }
}
