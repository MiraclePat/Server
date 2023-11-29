package com.miraclepat.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPatDto {
    //id, 대표 이미지, 제목, 시작일, 카테고리, 현재 인원, 최대 인원, 주소, 인증 빈도
    //state, isCompleted

    Long id;
    String repImg;
    String patName;
    @JsonFormat(pattern = "M월 d일(E)")
    LocalDate startDate;
    String category;
    int nowPerson;
    int maxPerson;
    String location;
    String days;
    State state;

    @JsonProperty("isCompleted")
    boolean completed = false; //당일의 인증 데이터가 있는가?


    public MyPatDto(Pat pat){
        this.id = pat.getId();
        this.patName = pat.getPatName();
        this.category = pat.getCategory().getCategoryName();
        this.location = pat.getLocation();
        this.startDate = pat.getStartDate();
        this.nowPerson = pat.getNowPerson();
        this.maxPerson = pat.getMaxPerson();
        this.days = pat.getDays();
        this.repImg = Constants.REP_IMG;
        this.state = pat.getState();
    }

    public void setCompleted(boolean isCompleted){
        this.completed = isCompleted;
    }

    public void setRepImg(String url){
        this.repImg = url;
    }
}
