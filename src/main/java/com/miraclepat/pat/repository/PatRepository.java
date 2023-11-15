package com.miraclepat.pat.repository;

import com.miraclepat.pat.entity.Pat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

public interface PatRepository extends JpaRepository<Pat, Long> {
    Optional<Pat> findById(Long id);
}
