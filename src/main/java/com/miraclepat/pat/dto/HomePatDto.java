package com.miraclepat.pat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomePatDto {
    //id, 대표 이미지, 제목, 시작일, 카테고리, 현재 인원, 최대 인원

    Long id;

    String repImg;

    String patName;

    @JsonFormat(pattern = "M.d(E)")
    LocalDate startDate;

    String category;

    String location;

    int nowPerson;

    int maxPerson;


    public HomePatDto(Pat pat){
        this.id = pat.getId();
        this.patName = pat.getPatName();
        this.category = pat.getCategory().getCategoryName();
        this.location = pat.getLocation();
        this.repImg = Constants.REP_IMG;
        this.startDate = pat.getStartDate();
        this.nowPerson = pat.getNowPerson();
        this.maxPerson = pat.getMaxPerson();
    }

    public void setRepImg(String url){
        this.repImg = url;
    }


}
