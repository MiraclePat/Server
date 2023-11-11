package com.miraclepat.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() { //현재 로그인한 사용자의 정보를 등록자와 수정자로 지정. 사용자의 정보를 반환

        /*Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication(); //인증 관련 정보 가져오기

        String userId="";

        if(authentication != null){
            userId = authentication.getName(); //현재 로그인한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 등록
        }*/

        String userId = UUID.randomUUID().toString();

        return Optional.of(userId);
    }
}
