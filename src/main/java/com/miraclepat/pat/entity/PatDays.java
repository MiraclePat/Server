package com.miraclepat.pat.entity;

import com.miraclepat.days.entity.Days;
import com.miraclepat.utils.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "pat_days_day_id_index", columnList = "days_id")})
@Getter //필수
@Setter //선택
@ToString //나중에 나오는 필드들에 대해 문자열을 자동으로 만들어줌
@NoArgsConstructor//빈생성자
public class PatDays extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    @Column(name = "pat_days_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pat_id")
    private Pat pat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "days_id")
    private Days days;

    public PatDays(Pat pat, Days days) {
        this.pat = pat;
        this.days = days;
    }
}
