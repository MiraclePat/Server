package com.miraclepat.auth.service;

import com.miraclepat.auth.dto.KakaoUserInfo;
import com.miraclepat.global.exception.CustomException;
import com.miraclepat.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class KakaoService {
    //유저가 로그인 시 보낸 카카오 액세스 토큰을 통해 카카오에서 유저 정보를 받아온다.

    private final WebClient webClient;

    public KakaoService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public KakaoUserInfo getKakaoUserInfo(String token) {

        try {
            //WebClient로 카카오 서버에 연결
            //액세스 토큰으로 사용자 정보를 받아온다.
            KakaoUserInfo kakaoUserInfo = webClient.get()
                    .uri("/v2/user/me")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()//응답값을 받는다.
                    .bodyToMono(KakaoUserInfo.class)//응답값이 KakaoUserInfo에 자동으로 매칭된다.
                    .block();

            return kakaoUserInfo;

        } catch (Exception e) {
            throw new CustomException(ErrorCode.KAKAO_CLIENT_EXCEPTION,
                    e.getMessage());
        }
    }


}
