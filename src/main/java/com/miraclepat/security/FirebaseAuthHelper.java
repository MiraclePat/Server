package com.miraclepat.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.miraclepat.global.exception.CustomException;
import com.miraclepat.global.exception.ErrorCode;
import com.miraclepat.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirebaseAuthHelper {

    private final FirebaseAuth firebaseAuth;

    //파이어베이스에 유저 추가
    public void createFirebaseUser(String userCode) {
        try {
            firebaseAuth.createUser(new UserRecord.CreateRequest().setUid(userCode));
        } catch (FirebaseAuthException e) {
            throw new CustomException(ErrorCode.FIREBASE_USER_CREATION_FAIL,
                    ErrorMessage.FAIL_USER_CREATION + ": " + e.getMessage());
        }
    }

    //커스텀 토큰 생성
    public String createFirebaseToken(String userCode) {

        try {
            //카카오 식별자로 토큰을 만들어 반환한다.
            return firebaseAuth.createCustomToken(userCode);
        } catch (FirebaseAuthException e) {
            throw new CustomException(ErrorCode.FIREBASE_CUSTOM_TOKEN_CREATION_FAIL,
                    ErrorMessage.FAIL_TOKEN_GENERATION + ": " + e.getMessage());
        }
    }

    //Uid
    public String getUid(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken).getUid();
        } catch (FirebaseAuthException e) {
            throw new CustomException(ErrorCode.INVALID_FIREBASE_ID_TOKEN,
                    ErrorMessage.FAIL_GET_UID + ": " + e.getMessage());
        }
    }

    //파이어베이스에 유저 삭제
    public void deleteUser(String userCode) {
        try {
            firebaseAuth.deleteUser(userCode);
        } catch (FirebaseAuthException e) {
            throw new CustomException(ErrorCode.FIREBASE_USER_WITHDRAW_FAIL,
                    ErrorMessage.FAIL_USER_DELETION + ": " + e.getMessage());
        }
    }
}
