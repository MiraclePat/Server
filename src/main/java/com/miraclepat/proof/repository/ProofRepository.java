package com.miraclepat.proof.repository;

import com.miraclepat.proof.entity.Proof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProofRepository extends JpaRepository<Proof, Long>,
        QuerydslPredicateExecutor<Proof>, ProofRepositoryCustom{

    @Query("SELECT p FROM Proof p WHERE p.patMember.id = :id")
    List<Proof> findAllByPatMemberId(@Param("id") Long id);

    @Query("SELECT p.id FROM Proof p WHERE p.patMember.id IN :ids")
    List<Long> findIdsByPatMemberIds(@Param("ids") List<Long> ids);

    @Query("SELECT p FROM Proof p WHERE p.patMember.id IN :ids")
    List<Proof> findAllByPatMemberIds(@Param("ids") List<Long> ids);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Proof p " +
            "WHERE p.patMember.id = :patMemberId AND p.createDate = :createDate")
    boolean existsTodayProof(@Param("patMemberId") Long patMemberId, @Param("createDate") LocalDate createDate);
}
