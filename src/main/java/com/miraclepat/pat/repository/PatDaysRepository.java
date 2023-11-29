package com.miraclepat.pat.repository;

import com.miraclepat.pat.entity.PatDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatDaysRepository extends JpaRepository<PatDays, Long> {
    Long countByPatId(Long patId);

    List<PatDays> findAllByPatId(Long patId);

    boolean existsByPatIdAndDaysId(Long patId, Long dayId);

    @Query("SELECT pd.id FROM PatDays pd WHERE pd.pat.id = :patId")
    List<Long> findIdsByPatId(@Param("patId") Long patId);
}
