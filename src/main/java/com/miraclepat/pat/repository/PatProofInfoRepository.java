package com.miraclepat.pat.repository;

import com.miraclepat.pat.entity.PatProofInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatProofInfoRepository extends JpaRepository<PatProofInfo, Long> {

    @Query("SELECT pi FROM PatProofInfo pi WHERE pi.pat.id = :id")
    Optional<PatProofInfo> findByPatId(@Param("id") Long id);

    @Query("SELECT pi.id FROM PatProofInfo pi WHERE pi.pat.id = :id")
    Long findIdByPatId(@Param("id") Long id);

}
