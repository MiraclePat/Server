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

    //현재 인원, 최대 인원, days 추가
    int nowPerson;

    int maxPerson;

    String days;

    String location;

    //위도
    double latitude;

    //경도
    double longitude;

    public PatDto(Long id, String repImg, String patName, LocalDate startDate,
                  String category, Point position, int nowPerson, int maxPerson, String days, String location) {
        this.id = id;
        this.repImg = repImg;
        this.patName = patName;
        this.startDate = startDate;
        this.category = category;
        this.latitude = position.getY();
        this.longitude = position.getX();
        this.nowPerson = nowPerson;
        this.maxPerson = maxPerson;
        this.days = days;
        this.location = location;
    }

    public void setRepImg(String url) {
        this.repImg = url;
    }
}
