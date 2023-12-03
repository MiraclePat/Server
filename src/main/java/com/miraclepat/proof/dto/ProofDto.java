package com.miraclepat.proof.dto;

import lombok.Getter;

@Getter
public class ProofDto {

    Long id;
    String proofImg;

    public ProofDto(Long id, String proofImg) {
        this.id = id;
        this.proofImg = proofImg;
    }

    public void setProofImg(String url) {
        this.proofImg = url;
    }
}
