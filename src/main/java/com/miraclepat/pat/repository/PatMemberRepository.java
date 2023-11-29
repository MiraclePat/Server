package com.miraclepat.pat.repository;

import com.miraclepat.pat.entity.PatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatMemberRepository extends JpaRepository<PatMember, Long> {

    @Query("SELECT pm FROM PatMember pm WHERE pm.pat.id = :patId AND pm.member.id = :memberId")
    Optional<PatMember> findByPatIdAndMemberId(@Param("patId") Long patId, @Param("memberId") Long memberId);

    @Query("SELECT pm.id FROM PatMember pm WHERE pm.pat.id = :patId AND pm.member.id = :memberId")
    Optional<Long> findIdByPatIdAndMemberId(@Param("patId") Long patId, @Param("memberId") Long memberId);

    @Query("SELECT pm.id FROM PatMember pm WHERE pm.pat.id = :patId")
    List<Long> findIdsByPatId(@Param("patId") Long patId);

    @Query("SELECT pm.id FROM PatMember pm WHERE pm.member.id = :MemberId")
    List<Long> findIdsByMemberId(@Param("MemberId") Long MemberId);

    @Query("SELECT pm FROM PatMember pm WHERE pm.pat.id = :patId")
    List<PatMember> findByPatId(@Param("patId") Long patId);

    @Query("SELECT CASE WHEN COUNT(pm) > 0 THEN true ELSE false END " +
            "FROM PatMember pm " +
            "WHERE pm.pat.id = :patId AND pm.member.id = :memberId")
    boolean existsByPatIdAndMemberId(@Param("patId") Long patId, @Param("memberId") Long memberId);

    @Query("SELECT pm.pat.id FROM PatMember pm WHERE pm.member.id = :memberId")
    List<Long> findJoinPatIdsByMemberId(@Param("memberId") Long memberId);
}
