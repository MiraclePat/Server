package com.miraclepat.pat.entity;

import com.miraclepat.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter //필수
@Setter //선택
@ToString //나중에 나오는 필드들에 대해 문자열을 자동으로 만들어줌
@NoArgsConstructor//빈생성자
public class PatMember {
    //하나의 챌린지에 참여중인 member

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    @Column(name = "pat_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pat_id")
    private Pat pat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
