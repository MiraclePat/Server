package com.miraclepat.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignupDto {

    private String userCode;

    private String age; //연령대

    private String email;

}
