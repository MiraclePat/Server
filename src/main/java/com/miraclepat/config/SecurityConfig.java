package com.miraclepat.config;

import com.google.firebase.auth.FirebaseAuth;
import com.miraclepat.security.CustomAuthenticationEntryPoint;
import com.miraclepat.security.FirebaseTokenFilter;
import com.miraclepat.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
회원 인증이 필요한 페이지 접근 시
 */

@Configuration
@EnableWebSecurity
/*
@RequiredArgsConstructor
@Component
*/
public class SecurityConfig{

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private FirebaseAuth firebaseAuth;

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
                .antMatchers("/api/v1/pats/home", "/api/v1/pats/map").permitAll()
                .antMatchers("/**/index.html").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/pats/{pat-id}").permitAll()

                .antMatchers("/api/test/**").permitAll()

                //.antMatchers("/**").permitAll()

                .and()
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new FirebaseTokenFilter(userDetailsService, firebaseAuth),
                        UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                //인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                .build();
    }
}

