package com.miraclepat.member.entity;

import com.miraclepat.member.constant.Role;
import com.miraclepat.auth.dto.SignupDto;
import com.miraclepat.utils.Constants;
import com.miraclepat.utils.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {
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

    private String email;

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean push;

    private String fcmToken;

    public static Member createMember(SignupDto signupDto){

        Member member = new Member();

        member.setRole(Role.ROLE_USER);
        member.userCode = signupDto.getUserCode();
        member.setProfileImg(Constants.REP_IMG);

        return member;

    }

}
