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

    private List<ProofDto> proofDtoList = new ArrayList<>();

    private Long totalPages;

    private Long totalCount;

    @Builder
    public ProofListDto(List<Proof> proofList, Long totalPages, Long totalCount){
        this.proofDtoList = proofList.stream()
                .map(ProofDto::new).collect(Collectors.toList());
        this.totalPages = totalPages;
        this.totalCount = totalCount;

    }
}
