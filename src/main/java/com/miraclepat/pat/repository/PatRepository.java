package com.miraclepat.pat.repository;

import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.dto.WriterInfoDto;
import com.miraclepat.pat.entity.Pat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatRepository extends JpaRepository<Pat, Long>,
        QuerydslPredicateExecutor<Pat>, PatRepositoryCustom{
    Optional<Pat> findById(Long id);

    @Query("SELECT COUNT(p) FROM Pat p WHERE p.member.id = :memberId")
    int countByMemberId(Long memberId);

    @Query("SELECT COUNT(p) FROM Pat p WHERE p.id in :patIds and p.state = :state")
    int countByState(@Param("patIds") List<Long> patIds, @Param("state") State state);

    @Query("SELECT p.member.id FROM Pat p WHERE p.id = :id")
    Optional<Long> findMemberIdByPatId(@Param("id") Long id);

    @Query("SELECT p.state FROM Pat p WHERE p.id = :id")
    Optional<State> findStateByPatId(@Param("id") Long id);

    @Query("SELECT new com.miraclepat.pat.dto.WriterInfoDto(m.id, m.nickname, m.profileImg) " +
            "FROM Pat p join p.member m " +
            "WHERE p.id = :id")
    Optional<WriterInfoDto> findMemberInfoByPatId(@Param("id") Long id);

    @Query("SELECT p.id FROM Pat p WHERE p.member.id = :memberId")
    List<Long> findOpenPatIdsByMemberId(@Param("memberId") Long memberId);
}
