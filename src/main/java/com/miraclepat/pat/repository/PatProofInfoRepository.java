package com.miraclepat.pat.repository;

import com.miraclepat.global.exception.ErrorMessage;
import com.miraclepat.pat.entity.PatProofInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface PatProofInfoRepository extends JpaRepository<PatProofInfo, Long> {

    @Query("SELECT pi FROM PatProofInfo pi JOIN FETCH pi.pat p WHERE p.id = :id")
    Optional<PatProofInfo> findByPatId(@Param("id") Long id);

    default PatProofInfo getByPatId(Long id) {
        return findByPatId(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_PAT));
    }

    @Query("SELECT pi.id FROM PatProofInfo pi WHERE pi.pat.id = :id")
    Long findIdByPatId(@Param("id") Long id);

}
