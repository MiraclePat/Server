package com.miraclepat.pat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.miraclepat.pat.constant.ButtonState;
import com.miraclepat.pat.constant.ImgType;
import com.miraclepat.pat.entity.Pat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatDetailDto {

    private Long patId;

    private String repImg;

    private String category;

    private String patName;

    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(pattern = "M월 d일(E)")
    private LocalDate modifiedStartDate;

    @JsonFormat(pattern = "M월 d일(E)")
    private LocalDate modifiedEndDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private List<String> dayList = new ArrayList<>();

    private String proofDetail;

    //옳은 예시 이미지
    private String correctImg;

    //틀린 예시 이미지
    private String incorrectImg;

    //본문 이미지
    private List<String> bodyImg = new ArrayList<>();

    @JsonProperty("realtime") //안붙이면 rest docs 오류
    private boolean realtime; //실시간으로 제한? 아니면 상관 없음?

    //------------
    private String patDetail;

    private int nowPerson;

    private int maxPerson;

    @JsonProperty("isWriter")
    private boolean writer = false; //작성자인지 아닌지.

    @JsonProperty("isJoiner")
    private boolean joiner = false; //참여자인지 아닌지.

    private String nickname;

    private String profileImg;

    //버튼 활성화 상태. "CANCELABLE": 변경 가능, "NO_CANCELABLE": 변경 불가,
    // "IN_PROGRESS": 진행중, "COMPLETED": 진행 완료
    private ButtonState state;

    public void setPat(Pat pat){
        this.patId = pat.getId();
        this.patName = pat.getPatName();
        this.patDetail = pat.getPatDetail();
        this.startDate = pat.getStartDate();
        this.endDate = pat.getEndDate();
        this.modifiedStartDate = pat.getStartDate();
        this.modifiedEndDate=pat.getEndDate();
        this.nowPerson = pat.getNowPerson();
        this.maxPerson = pat.getMaxPerson();
        if (pat.getLocation() != null) {
            this.location = pat.getLocation();
        }
        this.category = pat.getCategory().getCategoryName();
        this.startTime = pat.getStartTime();
        this.endTime = pat.getEndTime();
        this.dayList = pat.getPatDaysList().stream()
                .map(patDays -> patDays.getDays().getDayName())
                .collect(Collectors.toList());
        this.proofDetail = pat.getProofDetail();
        this.realtime = pat.isRealtime();
    }

    public void setImg(String img, ImgType type){
        if (type == ImgType.REPRESENTATIVE){
            this.repImg = img;
        } else if (type == ImgType.CORRECT) {
            this.correctImg = img;
        } else if (type == ImgType.INCORRECT) {
            this.incorrectImg = img;
        } else if (type == ImgType.BODY) {
            this.bodyImg.add(img);
        }
    }

    public void setIsWriterAndIsJoiner(boolean isWriter, boolean isJoiner){
        this.writer = isWriter;
        this.joiner = isJoiner;
    }

    public void setWriterProfile(String nickname, String profileImg){
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public void setState(ButtonState state){
        this.state = state;
    }
}
