package com.miraclepat.member.dto;

import com.miraclepat.utils.Constants;
import com.miraclepat.category.entity.Category;
import com.miraclepat.pat.entity.Pat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MyPatDto {
    //id, 대표 이미지, 제목, 시작일, 카테고리, 현재 인원, 최대 인원, 주소, 인증 빈도

    Long id;

    String repImg;

    String patName;

    LocalDate startDate;

    String category;

    int nowPerson;

    int maxPerson;

    String location;

    String days;



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
    }
}
