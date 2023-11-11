package com.miraclepat.proof.dto;

import com.miraclepat.proof.entity.Proof;
import lombok.Getter;

@Getter
public class ProofDto {

    Long id;

    String proofImg;


    public ProofDto(Proof proof){
        this.id = proof.getId();
        this.proofImg = proof.getProofImg();

    }
}
