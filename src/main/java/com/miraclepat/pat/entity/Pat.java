package com.miraclepat.pat.entity;

import com.miraclepat.category.entity.Category;
import com.miraclepat.member.entity.Member;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.dto.CreatePatDto;
import com.miraclepat.utils.entity.BaseTimeEntity;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "pat_state_index", columnList = "state"),
        @Index(name = "pat_member_id_index", columnList = "member_id"),
        @Index(name = "pat_category_id_index", columnList = "category_id")})
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Pat extends BaseTimeEntity {
    //작성자, 제목, 상세설명, 현재 인원, 최대 인원, 좌표, 주소, 카테고리
    //인증 시작시간, 인증 마감 시간, 챌린지 시작일, 챌린지 마감일, 인증 요일, 인증 방법 설명, 인증 방법

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    @Column(name = "pat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //카테고리
    @JoinColumn(name="category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    //챌린지 제목
    @Column(nullable = false)
    private String patName;

    //챌린지 상세내용
    @Lob
    @Column(nullable = false)
    private String patDetail;

    //인증 방법 설명
    @Column(nullable = false)
    private String proofDetail;

    private boolean realtime; //실시간으로 제한? 아니면 상관 없음?

    @Column(nullable = false)
    private String repImg;

    //현재 참가 인원수
    @Column(nullable = false)
    private int nowPerson;

    //최대 인원수
    @Column(nullable = false)
    private int maxPerson;

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

    @Enumerated(EnumType.STRING)
    private State state; //진행 상태

    //좌표
    @Column(columnDefinition = "POINT SRID 4326")
    private Point longLat;

    //주소
    @Builder.Default
    private String location="";

    //선택 요일을 String으로 저장 ex)월, 수, 목
    @Column(nullable = false)
    private String days;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pat",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<PatDays> patDaysList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pat",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<PatImg> patImgList = new ArrayList<>();

    public void updatePat(CreatePatDto createPatDto, Category category, String repImg) {
        this.patName = createPatDto.getPatName();
        this.patDetail = createPatDto.getPatDetail();
        this.maxPerson = createPatDto.getMaxPerson();
        this.startTime = createPatDto.getStartTime();
        this.endTime = createPatDto.getEndTime();
        this.startDate = createPatDto.getStartDate();
        this.endDate = createPatDto.getEndDate();
        this.proofDetail = createPatDto.getProofDetail();
        this.realtime = createPatDto.isRealtime();
        this.state = State.SCHEDULED;
        this.category = category;
        this.repImg = repImg;
    }

    public void setLocationAndPoint(String location, Point longLat) {
        this.longLat = longLat;
        this.location = location;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public void updateTime(){
        this.updateTime = LocalDateTime.now();
    }

    public void setPatImgList(List<PatImg> patImgList){
        this.patImgList.clear();
        this.patImgList = patImgList;
    }

    public void setPatDaysList(List<PatDays> patDaysList){
        this.patDaysList.clear();
        this.patDaysList = patDaysList;
    }

    public void setRepImg(String repImg){
        this.repImg = repImg;
    }

    public void setDays(String days){
        this.days = days;
    }

    public boolean addPerson(){
        if (this.nowPerson < this.maxPerson) {
            this.nowPerson += 1;
            return true;
        }
        return false;
    }

    public boolean subPerson(){
        if (this.nowPerson > 1){
            this.nowPerson -=1;
            return true;
        }
        return false;
    }

    public void deleteMember(){
        this.member = null;
    }
}
