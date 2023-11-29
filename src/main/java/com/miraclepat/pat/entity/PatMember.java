package com.miraclepat.pat.entity;

import com.miraclepat.member.entity.Member;
import com.miraclepat.utils.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "pat_member_pat_id_index", columnList = "pat_id"),
        @Index(name = "pat_member_member_id_index", columnList = "member_id")})
@Getter //필수
@ToString //나중에 나오는 필드들에 대해 문자열을 자동으로 만들어줌
@NoArgsConstructor
@AllArgsConstructor
public class PatMember extends BaseTimeEntity {
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

    @ColumnDefault("0")
    @Column(name = "proof_count")
    private int proofCount;

    public PatMember(Pat pat, Member member) {
        this.pat = pat;
        this.member = member;
    }

    public void addProofCount(){
        this.proofCount += 1;
    }

    public void deleteMember(){
        this.member = null;
    }
}
