package com.miraclepat.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoUserInfo {
    //고유id
    @JsonProperty("id")
    private String userCode;

}
