package com.miraclepat.auth.controller;

import com.miraclepat.auth.dto.SignupDto;
import com.miraclepat.auth.dto.TokenDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/test/auth")
public class TestAuthController {

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupDto SignupDto){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody TokenDto tokenDto){
        TokenDto tr = new TokenDto("토큰");
        return ResponseEntity.ok(tr);
    }

}
