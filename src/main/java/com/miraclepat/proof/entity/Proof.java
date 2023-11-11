package com.miraclepat.proof.entity;

import com.miraclepat.pat.entity.Pat;
import com.miraclepat.member.entity.Member;
import com.miraclepat.utils.entity.BaseTimeEntity;
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
public class Proof extends BaseTimeEntity {
    //챌린지, 멤버, 이미지, 원래 이미지 이름, 관리용 이미지 이름, 인증일

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proof_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pat_id")
    private Pat pat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String imgName; //관리할 때 사용할 이름

    private String oriImgName; //원래 이미지 이름

    private String proofImg;  //이미지 경로

}
