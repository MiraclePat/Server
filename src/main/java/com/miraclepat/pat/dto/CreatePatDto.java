package com.miraclepat.pat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
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
    @Max( value = 10000, message = "최대 인원은 10000명입니다.")
    int maxPerson;

    //경도
    Double longitude;

    //위도
    Double latitude;

    //상세주소
    String location;

    //카테고리
    @NotBlank(message = "카테고리를 지정해주세요.")
    String category;

    @NotNull(message = "시작 시간을 지정해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime startTime;

    @NotNull(message = "마감 시간을 지정해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime endTime;

    @NotNull(message = "시작일을 지정해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate startDate;

    @NotNull(message = "종료일을 지정해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate endDate;

    @NotBlank(message = "인증 방법을 작성해주세요.")
    @Size(min = 5, max = 300,message = "5-300자 사이로 작성해주세요.")
    String proofDetail;

    @NotNull(message = "인증 요일을 지정해주세요.")
    List<String>days = new ArrayList<>();

    //실시간 인증? or 사진 인증도 가능?
    @NotNull(message = "인증 수단을 지정해주세요.")
    @JsonProperty("realtime") //안붙이면 rest docs 오류
    boolean realtime;
}
