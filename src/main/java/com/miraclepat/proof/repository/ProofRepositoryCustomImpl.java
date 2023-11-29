package com.miraclepat.proof.repository;

import com.miraclepat.proof.dto.ProofDto;
import com.miraclepat.proof.dto.ProofListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.miraclepat.proof.entity.QProof.proof;

public class ProofRepositoryCustomImpl implements ProofRepositoryCustom{
    private JPAQueryFactory queryFactory;
    public ProofRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ProofListDto getMyProof(Long lastId, int size, Long id) {
        List<ProofDto> proofDtoList = queryFactory
                .select(Projections.constructor(ProofDto.class, proof.id, proof.imgName))
                .from(proof)
                .where(predicateByLastId(lastId),
                        proof.patMember.id.eq(id))
                .orderBy(proof.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = proofDtoList.size() > size;
        if (hasNext) {
            proofDtoList.remove(proofDtoList.size() - 1);
        }

        ProofListDto response = new ProofListDto(proofDtoList, hasNext);
        return response;
    }

    @Override
    public ProofListDto getAnotherProof(Long lastId, int size, List<Long> ids) {
        List<ProofDto> proofDtoList = queryFactory
                .select(Projections.constructor(ProofDto.class, proof.id, proof.imgName))
                .from(proof)
                .where(predicateByLastId(lastId),
                        proof.patMember.id.in(ids))
                .orderBy(proof.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = proofDtoList.size() > size;
        if (hasNext) {
            proofDtoList.remove(proofDtoList.size() - 1);
        }

        ProofListDto response = new ProofListDto(proofDtoList, hasNext);
        return response;
    }

    //id
    private BooleanExpression predicateByLastId(Long lastId) {
        if (lastId == null) {
            return null;
        }
        return proof.id.lt(lastId);
    }
}
