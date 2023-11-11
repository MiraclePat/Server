package com.miraclepat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //JPA의 Auditing 기능 활성화
public class AuditConfig { //공통,자동화. 감사 정보를 설정하고 감사 설정을 구성하는 데 사용

    @Bean
    public AuditorAware<String> auditorProvider(){ //등록자와 수정자를 처리해주는 AuditorAware을 빈으로 등록
        return new AuditorAwareImpl();
    }
}
