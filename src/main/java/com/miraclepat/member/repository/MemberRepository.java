package com.miraclepat.member.repository;

import com.miraclepat.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Member findByUserCode(String userCode);

    boolean existsByUserCode(String userCode);

    boolean existsById(Long id);

    boolean existsByNickname(String nickname);

    @Query("select m.nickname, m.profileImg from Member m where m.id = :id")
    Optional<Object[]> findNicknameAndProfileImgById(@Param("id") Long id);


}
