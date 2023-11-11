package com.miraclepat.member.dto;

import javax.validation.constraints.NotBlank;

public class SignupDto {

    @NotBlank(message = "닉네임을 작성해주세요.")
    private String nickname;

    @NotBlank(message = "이메일을 작성해주세요.")
    private String email;

    @NotBlank(message = "프로필 이미지를 선택해주세요.")
    private String profileImgUrl;

}
