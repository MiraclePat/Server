package com.miraclepat.pat.dto;

import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PatDetailDto {

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

    private String realtime; //실시간으로 제한? 아니면 상관 없음?

    //------------
    private String patDetail;

    private int nowPerson;

    private int maxPerson;

    public PatDetailDto(Pat pat){
        this.patId = pat.getId();
        this.patName = pat.getPatName();
        this.patDetail = pat.getPatDetail();
        this.repImg = Constants.REP_IMG;
        this.startDate = pat.getStartDate();
        this.nowPerson = pat.getNowPerson();
        this.maxPerson = pat.getMaxPerson();
        //this.leader = pat.getLeader();
        this.location = pat.getLocation();
        this.category = pat.getCategory().getCategoryName();
        this.startTime = pat.getStartTime();
        this.endTime = pat.getEndTime();
        this.endDate = pat.getEndDate();
        this.days = pat.getDays();
        this.proofDetail = pat.getProofDetail();
        this.realtime = pat.getRealtime();

        for(int i=0; i<4; i++){
            bodyImg.add(Constants.BODY_IMG);
        }

        for(int i=0; i<3; i++){
            incorrectImg.add(Constants.INCORRECT_IMG);
        }
        correctImg=Constants.CORRECT_IMG;
        //this.maxProof = pat.getMaxProof();
    }
}
