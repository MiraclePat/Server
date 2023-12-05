package com.miraclepat.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirebaseAuthHelper {
    //파이어베이스에 유저 추가, 삭제
    //커스텀 토큰 생성
    //getUid

    private final FirebaseAuth firebaseAuth;

    public void createFirebaseUser(String userCode) {
        try {
            firebaseAuth.createUser(new UserRecord.CreateRequest().setUid(userCode));
        }catch (FirebaseAuthException e){
            //예외 발생 처리

            log.info("유저를 생성하지 못했습니다.");
        }
    }

    public String createFirebaseToken(String userCode) {

        try{
            //카카오 식별자로 토큰을 만들어 반환한다.
            return firebaseAuth.createCustomToken(userCode);
        }catch (FirebaseAuthException e){
            //예외 처리
            return "토큰 생성 실패";
        }
    }

    public String getUid(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken).getUid();
        }catch (FirebaseAuthException e){
            //예외처리
            return "Uid 얻어오기 실패";
        }
    }

    public void deleteUser(String userCode) {
        try{
            firebaseAuth.deleteUser(userCode);
        }catch (FirebaseAuthException e){
            log.error("탈퇴 불가");
        }
    }
}
