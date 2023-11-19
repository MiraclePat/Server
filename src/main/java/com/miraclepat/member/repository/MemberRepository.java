package com.miraclepat.member.repository;

import com.miraclepat.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Member findByUserCode(String userCode);

    boolean existsByUserCode(String userCode);

    boolean existsById(Long id);

    boolean existsByNickname(String nickname);

}
