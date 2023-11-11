package com.miraclepat.pat.dto;

import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
public class CreatePatDto {

    //팟 제목
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 2, max = 20,message = "2-20자 사이로 작성해주세요.")
    String patName;

    //팟 상세내용
    @NotBlank(message = "상세 내용을 입력해주세요.")
    @Size(min = 15, max = 500,message = "15-500자 사이로 작성해주세요.")
    String patDetail;

    //최대 인원
    @NotNull(message = "최대 참여 인원을 지정해주세요.")
    @Max( value = 10000, message = "15-500자 사이로 작성해주세요.")
    int maxPerson;

    //위도
    double latitude;

    //경도
    double longitude;

    //상세주소
    String location;

    //카테고리
    @NotBlank(message = "카테고리를 지정해주세요.")
    String category;

    @NotBlank(message = "시작 시간을 지정해주세요.")
    String startTime;

    @NotBlank(message = "마감 시간을 지정해주세요.")
    String endTime;

    @NotBlank(message = "시작일을 지정해주세요.")
    String startDate;

    @NotBlank(message = "종료일을 지정해주세요.")
    String endDate;

    @NotBlank(message = "인증 방법을 작성해주세요.")
    @Size(min = 5, max = 300,message = "5-300자 사이로 작성해주세요.")
    String proofDetail;


    //인증 요일
    @NotBlank(message = "인증 요일을 지정해주세요.")
    String days;

    //실시간 인증? or 사진 인증도 가능?
    @NotBlank(message = "인증 수단을 지정해주세요.")
    String realtime;
}
