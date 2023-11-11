package com.miraclepat.home.dto;

import com.miraclepat.category.entity.Category;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class HomePatDto {
    //id, 대표 이미지, 제목, 시작일, 카테고리, 현재 인원, 최대 인원

    Long id;

    String repImg;

    String patName;

    LocalDate startDate;

    String category;

    int nowPerson;

    int maxPerson;


    public HomePatDto(Pat pat){
        this.id = pat.getId();
        this.patName = pat.getPatName();
        this.category = pat.getCategory().getCategoryName();
        this.repImg = Constants.REP_IMG;
        this.startDate = pat.getStartDate();
        this.nowPerson = pat.getNowPerson();
        this.maxPerson = pat.getMaxPerson();
    }
}
