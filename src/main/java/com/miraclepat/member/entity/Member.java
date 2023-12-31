package com.miraclepat.member.entity;

import com.miraclepat.member.constant.Role;
import com.miraclepat.auth.dto.SignupDto;
import com.miraclepat.utils.entity.BaseModifiableEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter //삭제
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseModifiableEntity {
    //멤버 엔티티
    //닉네임, 프로필, 이메일, role, 알람 설정, fcm토큰, 오픈 팟

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String userCode;

    //닉네임은 중복 불가
    @Column(unique = true)
    private String nickname;

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean push;

    private String fcmToken;

    public static Member createMember(SignupDto signupDto){

        Member member = new Member();

        member.setRole(Role.ROLE_USER);
        member.userCode = signupDto.getUserCode();
        //프로필 조회할 때 값이 null이면 기본 이미지를 주는 것으로.

        return member;

    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateProfileImg(String profileImg){
        this.profileImg = profileImg;
    }

    public void updatePush(boolean push){
        this.push = push;
    }

}
