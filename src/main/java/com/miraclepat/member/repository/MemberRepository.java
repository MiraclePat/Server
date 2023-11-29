package com.miraclepat.member.repository;

import com.miraclepat.member.dto.ProfileDto;
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

    @Query("SELECT new com.miraclepat.member.dto.ProfileDto(m.profileImg, m.nickname) " +
            "FROM Member m WHERE m.id = :id")
    Optional<ProfileDto> findNicknameAndProfileImgById(@Param("id") Long id);

    @Query("SELECT m.userCode FROM Member m WHERE m.id = :id")
    String findUserCodeById(@Param("id") Long id);


}
