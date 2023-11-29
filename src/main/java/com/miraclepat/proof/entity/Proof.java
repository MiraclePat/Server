package com.miraclepat.proof.entity;

import com.miraclepat.pat.entity.PatMember;
import com.miraclepat.utils.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(indexes = {
        @Index(name = "proof_pat_member_id_index", columnList = "pat_member_id")})
@Getter
@NoArgsConstructor
public class Proof extends BaseTimeEntity {
    //챌린지, 멤버, 이미지, 원래 이미지 이름, 관리용 이미지 이름, 인증일

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proof_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pat_member_id")
    private PatMember patMember;

    private LocalDate createDate;

    private String imgName; //관리할 때 사용할 이름

    private String oriImgName; //원래 이미지 이름

    @Builder
    public Proof(PatMember patMember, LocalDate createDate, String imgName, String oriImgName) {
        this.patMember = patMember;
        this.createDate = createDate;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
    }
}
