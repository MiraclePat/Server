package com.miraclepat.pat.dto;

import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PatDto {
    //id, 대표 이미지, 제목, 시작일, 카테고리, 위도, 경도

    Long id;

    String repImg;

    String patName;

    LocalDate startDate;

    String category;

    //위도
    double latitude;

    //경도
    double longitude;


    public PatDto(Pat pat){
        this.id = pat.getId();
        this.patName = pat.getPatName();
        this.category = pat.getCategory().getCategoryName();
        this.repImg = Constants.REP_IMG;
        this.startDate = pat.getStartDate();
        this.latitude = pat.getPosition().getX();
        this.longitude = pat.getPosition().getY();
    }
}
