package com.miraclepat.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileDto {
    String profileImg;
    String nickname;
    int finPats; //완료한 팟 수
    int openPats; //개설한 팟 수

    public ProfileDto(String profileImg, String nickname, int finPats, int openPats) {
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.finPats = finPats;
        this.openPats = openPats;
    }

    public ProfileDto(String profileImg, String nickname) {
        this.profileImg = profileImg;
        this.nickname = nickname;
    }

    public void setPats(int finPats, int openPats){
        this.finPats = finPats;
        this.openPats = openPats;
    }

    public void setProfileImg(String url){
        this.profileImg = url;
    }
}
