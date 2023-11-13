package com.miraclepat.proof.dto;
import com.miraclepat.proof.entity.Proof;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
public class ProofListDto {

    private List<ProofDto> content = new ArrayList<>();

    private Boolean hasNext;

    @Builder
    public ProofListDto(List<Proof> proofList){
        this.content = proofList.stream()
                .map(ProofDto::new).collect(Collectors.toList());
        this.hasNext = true;

    }
}
