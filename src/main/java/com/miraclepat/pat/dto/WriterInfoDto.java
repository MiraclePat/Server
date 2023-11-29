package com.miraclepat.pat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WriterInfoDto {
    Long id;
    String nickname;
    String profileImg;

    public WriterInfoDto(Long id, String nickname, String profileImg) {
        this.id = id;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public void setProfileImg(String url){
        this.profileImg = url;
    }
}
