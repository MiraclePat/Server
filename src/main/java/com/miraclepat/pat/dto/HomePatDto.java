package com.miraclepat.pat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    public void setRepImg(String url){
        this.repImg = url;
    }


}
