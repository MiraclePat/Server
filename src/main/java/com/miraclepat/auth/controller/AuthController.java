package com.miraclepat.auth.controller;

import com.miraclepat.auth.dto.KakaoUserInfo;
import com.miraclepat.auth.dto.SignupDto;
import com.miraclepat.auth.dto.TokenDto;
import com.miraclepat.auth.service.AuthService;
import com.miraclepat.auth.service.KakaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    //로그인, 회원가입을 처리한다.
    //로그인 -> 회원 검사 -> 없다면 회원가입

    @Autowired
    KakaoService kakaoService;
    @Autowired
    AuthService authService;

    //로그인
    @PostMapping("/login")
    public ResponseEntity login(
            @Valid
            @RequestBody TokenDto tokenDto) {
        //카카오 액세스 토큰을 받는다.
        //카카오 서비스에서 사용자 정보를 받아온다.
        KakaoUserInfo kakaoUserInfo = kakaoService.getKakaoUserInfo(tokenDto.getToken());

        try {
            //받아온 정보로 회원 검색을 한다. 가입된 정보가 있다면 로그인 토큰을 반환한다.
            TokenDto tokenDtoResponse = authService.kakaoLogin(kakaoUserInfo);
            return ResponseEntity.ok(tokenDtoResponse);
        } catch (NoSuchElementException e) {
            //AuthService에서 던진 예외처리
            //가입된 정보가 없다면 401 상태코드와 함께 카카오에서 받아온 유저 정보를 반환한다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(kakaoUserInfo);
        }
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupDto signupDto) {
        //가입되지 않은 회원으로 로그인 실패 시 전달했던 카카오 유저 정보를 받는다.
        //회원가입 한다.
        authService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
