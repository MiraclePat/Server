package com.miraclepat.member.dto;

import com.miraclepat.category.entity.Category;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyPatDetailDto {

    private Long patId;

    private String repImg;

    private String category;

    private String patName;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String days;

    private String proofDetail;

    //본문 이미지
    private List<String> bodyImg = new ArrayList<>();

    //옳은 예시 이미지
    private String correctImg;

    //틀린 예시 이미지
    private List<String> incorrectImg = new ArrayList<>();

    private boolean realtime; //실시간으로 제한? 아니면 상관 없음?

    //----

    private int maxProof; //인증 기간 * 횟수로 미리 계산 -> 내가 최대로 인증해야하는 수

    private int myProof; //내가 현재 인증한 수

    private int allProof; //전체가 인증한 수

    private int allMaxProof; //전체가 최대로 인증해야하는 수 -> maxProof*참여인원

    private int myFailProof;

    private int allFailProof;

    public MyPatDetailDto(Pat pat){
        this.patId = pat.getId();
        this.patName = pat.getPatName();
        this.repImg = "url";
        this.startDate = pat.getStartDate();
        this.location = pat.getLocation();
        this.category = pat.getCategory().getCategoryName();
        this.startTime = pat.getStartTime();
        this.endTime = pat.getEndTime();
        this.endDate = pat.getEndDate();
        this.days = pat.getDays();
        this.proofDetail = pat.getProofDetail();
        this.realtime = pat.isRealtime();
        this.maxProof = 15;
        this.allMaxProof = pat.getMaxProof()*pat.getNowPerson();

        this.myProof = 5;
        this.myFailProof = 3;

        this.allProof =55;
        this.allFailProof = 9;

        for(int i=0; i<4; i++){
            bodyImg.add(Constants.BODY_IMG);
        }

        for(int i=0; i<3; i++){
            incorrectImg.add(Constants.INCORRECT_IMG);
        }
        correctImg=Constants.CORRECT_IMG;
    }
}
