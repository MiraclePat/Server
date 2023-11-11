package com.miraclepat.member.dto;

import lombok.Getter;

@Getter
public class ProfileDto {
    String profileImg;
    String nickname;
    int finPats;
    int openPats;

    public ProfileDto(String profileImg, String nickname, int finPats, int openPats) {
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.finPats = finPats;
        this.openPats = openPats;
    }
}
