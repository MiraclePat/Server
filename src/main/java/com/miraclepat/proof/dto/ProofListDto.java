package com.miraclepat.proof.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class ProofListDto {

    private List<ProofDto> content = new ArrayList<>();

    private Boolean hasNext;

    public ProofListDto(List<ProofDto> content, Boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
}
