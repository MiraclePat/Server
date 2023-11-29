package com.miraclepat.pat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Table(indexes = {
        @Index(name = "pat_proof_info_pat_id_index", columnList = "pat_id")})
@NoArgsConstructor
public class PatProofInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    @Column(name = "pat_proof_info_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "pat_id")
    private Pat pat;

    @Column(nullable = false)
    private int maxProof; //인증 기간 * 횟수로 미리 계산

    @ColumnDefault("0")
    @Column(name = "pat_proof_count")
    private int proofCount;

    public PatProofInfo(int maxProof, Pat pat) {
        this.maxProof = maxProof;
        this.pat = pat;
    }

    public void setMaxProof(int maxProof){
        this.maxProof = maxProof;
    }

    public void addProofCount(){
        this.proofCount += 1;
    }

}
