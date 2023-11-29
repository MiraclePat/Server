package com.miraclepat.pat.repository;

import com.miraclepat.pat.entity.PatImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatImgRepository extends JpaRepository<PatImg, Long> {
    List<PatImg> findAllByPatId(Long patId);

    @Query("SELECT pi.id FROM PatImg pi WHERE pi.pat.id = :patId")
    List<Long> findIdsByPatId(@Param("patId") Long patId);
}
