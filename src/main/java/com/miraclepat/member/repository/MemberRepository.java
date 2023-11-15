package com.miraclepat.member.repository;

import com.miraclepat.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    boolean existsByUserCode(String userCode);

    boolean existsById(Long id);

    boolean existsByNickname(String nickname);

}
