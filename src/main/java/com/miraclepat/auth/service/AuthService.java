package com.miraclepat.auth.service;

import com.miraclepat.auth.dto.KakaoUserInfo;
import com.miraclepat.auth.dto.SignupDto;
import com.miraclepat.auth.dto.TokenDto;
import com.miraclepat.global.exception.CustomException;
import com.miraclepat.global.exception.ErrorCode;
import com.miraclepat.global.exception.ErrorMessage;
import com.miraclepat.member.entity.Member;
import com.miraclepat.member.repository.MemberRepository;
import com.miraclepat.security.FirebaseAuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Random;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final FirebaseAuthHelper firebaseAuthHelper;

    public TokenDto kakaoLogin(KakaoUserInfo kakaoUserInfo) {

        //카카오에서 받아온 식별자로 회원가입된 유저인지 찾는다.
        if (memberRepository.existsByUserCode(kakaoUserInfo.getUserCode())) {
            //있다면 식별자로 토큰을 생성해 반환한다.
            return new TokenDto(firebaseAuthHelper.createFirebaseToken(kakaoUserInfo.getUserCode()));
        }
        //없다면 예외를 발생시킨다.
        throw new NoSuchElementException(ErrorMessage.NOT_EXIST_MEMBER);
    }


    @Transactional
    public void signup(SignupDto signupDto) {
        //회원가입을 한다.
        //멤버에 저장 및 파이어베이스 회원 생성

        //카카오 식별자로 가입된 회원이 없는지 확인한다.
        if (memberRepository.existsByUserCode(signupDto.getUserCode())) {
            throw new CustomException(ErrorCode.EXIST_RESOURCE, ErrorMessage.EXIST_MEMBER);
        }

        //없다면 파이어베이스에 유저를 추가한다.
        firebaseAuthHelper.createFirebaseUser(signupDto.getUserCode());

        Member member = Member.createMember(signupDto);

        //랜덤 번호를 생성해 닉네임을 정해준다.1~1000000
        Random random = new Random();
        int num = random.nextInt(999999) + 1;
        while (memberRepository.existsByNickname("도전자" + num)) {
            num = random.nextInt(999999) + 1;
        }
        member.updateNickname("도전자" + num);

        memberRepository.save(member);

    }
}
