package com.miraclepat.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.miraclepat.pat.constant.ButtonState;
import com.miraclepat.pat.constant.ImgType;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPatDetailDto {
    //id, 대표 이미지 url, 타이틀, 시작일, 마감일, 시작 시간, 마감 시간, 인증 요일, 인증 방법, 인증 예시 사진 2종류
    //내 인증 수, 내 최대 인증 수
    //전체 인원 인증 수, 전체 최대 인증 수

    private Long patId;

    private String repImg;

    private String category;

    private String patName;

    private String location;

    @JsonFormat(pattern = "M월 d일")
    private LocalDate startDate;

    @JsonFormat(pattern = "M월 d일")
    private LocalDate endDate;

    @JsonFormat(pattern = "M월 d일(E)")
    private LocalDate modifiedStartDate;

    @JsonFormat(pattern = "M월 d일(E)")
    private LocalDate modifiedEndDate;

    @JsonFormat(pattern = "a h:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "a h:mm")
    private LocalTime endTime;

    private String days;

    @Builder.Default
    private List<String> dayList = new ArrayList<>();

    private String patDetail;

    private String proofDetail;

    //본문 이미지
    @Builder.Default
    private List<String> bodyImg = new ArrayList<>();

    //옳은 예시 이미지
    private String correctImg;

    //틀린 예시 이미지
    private String incorrectImg;

    private boolean realtime; //실시간으로 제한? 아니면 상관 없음?

    //----

    private int maxProof; //인증 기간 * 횟수로 미리 계산 -> 내가 최대로 인증해야하는 수

    private int myProof; //내가 현재 인증한 수

    private int allProof; //전체가 인증한 수

    private int allMaxProof; //전체가 최대로 인증해야하는 수 -> maxProof*참여인원

    private int myFailProof; //조회 당일 기준 인증해야하는 수 - 현재 인증 수

    private int allFailProof;

    //-----
    ButtonState state;

    @JsonProperty("isCompleted")
    @Builder.Default
    boolean completed = false; //당일의 인증 데이터가 있는가?

    //testController용
    public MyPatDetailDto(Pat pat){
        this.patId = pat.getId();
        this.patName = pat.getPatName();
        this.patDetail = pat.getPatDetail();
        this.repImg = Constants.REP_IMG;
        this.startDate = pat.getStartDate();
        this.location = pat.getLocation();
        this.category = pat.getCategory().getCategoryName();
        this.startTime = pat.getStartTime();
        this.endTime = pat.getEndTime();
        this.endDate = pat.getEndDate();
        this.modifiedStartDate = pat.getStartDate();
        this.modifiedEndDate = pat.getEndDate();
        this.days = pat.getDays();
        this.proofDetail = pat.getProofDetail();
        this.realtime = pat.isRealtime();
        this.maxProof = 15;
        this.allMaxProof = 75;
        this.dayList = new ArrayList<>();
        this.dayList.add("월요일");
        this.dayList.add("금요일");

        this.myProof = 5;
        this.myFailProof = 3;
        this.dayList.add("월요일");
        this.dayList.add("금요일");
        this.allProof =30;
        this.allFailProof = 10;
        this.state = ButtonState.IN_PROGRESS;

        this.bodyImg = new ArrayList<>();
        for(int i=0; i<4; i++){
            bodyImg.add(Constants.BODY_IMG);
        }
        incorrectImg=Constants.INCORRECT_IMG;
        correctImg=Constants.CORRECT_IMG;
    }

    public void setImg(String img, ImgType type){
        if (type == ImgType.REPRESENTATIVE){
            this.repImg = img;
        } else if (type == ImgType.CORRECT) {
            this.correctImg = img;
        } else if (type == ImgType.INCORRECT) {
            this.incorrectImg = img;
        } else if (type == ImgType.BODY) {
            this.bodyImg.add(img);
        }
    }

    public void setState(ButtonState state){
        this.state = state;
    }
}
