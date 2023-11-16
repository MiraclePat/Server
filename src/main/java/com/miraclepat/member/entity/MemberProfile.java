package com.miraclepat.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfile {

    //카카오톡 아이디?, 프로필, 이메일, 닉네임, 연령대, 성별

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_profile_id")
    private Long id;

    private String nickname;

    private String profileImg;
}
