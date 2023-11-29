package com.miraclepat.pat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PatDto {
    //id, 대표 이미지, 제목, 시작일, 카테고리, 위도, 경도

    Long id;

    String repImg;

    String patName;

    @JsonFormat(pattern = "M월 d일(E)")
    LocalDate startDate;

    String category;

    //위도
    double latitude;

    //경도
    double longitude;


    //testController
    public PatDto(Long id, String repImg, String patName, LocalDate startDate, String category, double latitude, double longitude) {
        this.id = id;
        this.repImg = repImg;
        this.patName = patName;
        this.startDate = startDate;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PatDto(Long id, String repImg, String patName, LocalDate startDate,
                  String category, Point position) {
        this.id = id;
        this.repImg = repImg;
        this.patName = patName;
        this.startDate = startDate;
        this.category = category;
        this.latitude = position.getY();
        this.longitude = position.getX();
    }

    public void setRepImg(String url){
        this.repImg = url;
    }
}
