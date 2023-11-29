package com.miraclepat.proof.repository;

import com.miraclepat.proof.dto.ProofListDto;

import java.util.List;

public interface ProofRepositoryCustom {
    ProofListDto getMyProof(Long lastId, int size, Long id);

    ProofListDto getAnotherProof(Long lastId, int size, List<Long> ids);
}
