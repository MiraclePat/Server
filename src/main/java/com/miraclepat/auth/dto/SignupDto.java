package com.miraclepat.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SignupDto {

    @JsonProperty("id")
    private String userCode;

}
