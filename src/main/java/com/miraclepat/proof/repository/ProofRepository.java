package com.miraclepat.proof.repository;

import com.miraclepat.proof.entity.Proof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ProofRepository extends JpaRepository<Proof, Long>,
        QuerydslPredicateExecutor<Proof>, ProofRepositoryCustom{

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Proof p " +
            "WHERE p.patMember.id = :patMemberId AND p.createDate = :createDate")
    boolean existsTodayProof(@Param("patMemberId") Long patMemberId, @Param("createDate") LocalDate createDate);
}
