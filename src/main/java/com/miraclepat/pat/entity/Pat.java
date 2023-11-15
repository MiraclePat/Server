package com.miraclepat.pat.entity;

import com.miraclepat.category.entity.Category;
import com.miraclepat.pat.constant.State;
import com.miraclepat.utils.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter //필수
@Setter //선택
@ToString //나중에 나오는 필드들에 대해 문자열을 자동으로 만들어줌
@NoArgsConstructor//빈생성자
public class Pat extends BaseEntity {
    //작성자, 제목, 상세설명, 현재 인원, 최대 인원, 좌표, 주소, 카테고리
    //인증 시작시간, 인증 마감 시간, 챌린지 시작일, 챌린지 마감일, 인증 요일, 인증 방법 설명, 인증 방법
    //최대 인증수(계산해서 넣기 )

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    @Column(name = "pat_id")
    private Long id;

    private String leader; //작성자의 닉네임 넣기

    //챌린지 제목
    @Column(nullable = false)
    private String patName;

    //챌린지 상세내용
    @Lob
    @Column(nullable = false)
    private String patDetail;

    //현재 참가 인원수
    @Column(nullable = false)
    private int nowPerson;

    //최대 인원수
    @Column(nullable = false)
    private int maxPerson;

    //좌표
    @Column(columnDefinition = "POINT SRID 4326")
    private Point position;

    //주소
    private String location;

    //카테고리
    @JoinColumn(name="category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    //인증 시작시간
    @Column(nullable = false)
    private LocalTime startTime;

    //인증 마감시간
    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    //선택 요일을 String으로 저장
    @Column(nullable = false)
    private String days;

    //인증 방법 설명
    @Column(nullable = false)
    private String proofDetail;

    private boolean realtime; //실시간으로 제한? 아니면 상관 없음?

    @Column(nullable = false)
    private int maxProof; //인증 기간 * 횟수로 미리 계산

    @Column(nullable = false)
    private String repImg;

    @Enumerated(EnumType.STRING)
    private State state; //진행 상태

    public Point createPoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
