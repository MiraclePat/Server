package com.miraclepat.config;

import com.miraclepat.security.CustomAuthenticationEntryPoint;
import com.miraclepat.security.FirebaseAuthHelper;
import com.miraclepat.security.FirebaseTokenFilter;
import com.miraclepat.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
회원 인증이 필요한 페이지 접근 시
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final FirebaseAuthHelper firebaseAuthHelper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/pats/home", "/api/v1/pats/home/banner", "/api/v1/pats/map").permitAll()
                .antMatchers("/**/index.html").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/pats/{pat-id}").permitAll()

                .antMatchers("/api/test/**").permitAll()

                .and()
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new FirebaseTokenFilter(userDetailsService, firebaseAuthHelper),
                        UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                //인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                .build();
    }
}

