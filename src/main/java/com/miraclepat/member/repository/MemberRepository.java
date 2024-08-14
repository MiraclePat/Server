package com.miraclepat.member.repository;

import com.miraclepat.global.exception.ErrorMessage;
import com.miraclepat.member.dto.ProfileDto;
import com.miraclepat.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_MEMBER_INFO));
    }

    Optional<Member> findByUserCode(String userCode);

    boolean existsByUserCode(String userCode);

    boolean existsById(Long id);

    boolean existsByNickname(String nickname);

    @Query("SELECT new com.miraclepat.member.dto.ProfileDto(m.profileImg, m.nickname) " +
            "FROM Member m WHERE m.id = :id")
    Optional<ProfileDto> findNicknameAndProfileImgById(@Param("id") Long id);

}
