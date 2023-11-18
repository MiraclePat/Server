package com.miraclepat.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FirebaseTokenFilter extends OncePerRequestFilter {

    //비회원은 조회(GET)만 가능하다.
    public static List<String> PERMIT_URI= Arrays.asList(
            "/api/v1/auth/", "/api/v1/pats/", "/api/test/", "/docs/");
    private UserDetailsServiceImpl userDetailsService;
    private FirebaseAuth firebaseAuth;

    public FirebaseTokenFilter(UserDetailsServiceImpl userDetailsService, FirebaseAuth firebaseAuth) {
        this.userDetailsService = userDetailsService;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if(!permitUri(request)){
            //헤더에서 토큰을 가져온다.
            log.info("FirebaseTokenFilter Start====");
            FirebaseToken decodedToken;

            // verify IdToken
            try{
                String header = request.getHeader(HttpHeaders.AUTHORIZATION);

                if (header == null || !header.startsWith("Bearer ")) {
                    log.info("유효한 토큰이 없어요.");
                    throw new IllegalArgumentException("Bearer 형식의 토큰이 아닙니다.");
                }
                String token = header.substring(7);

                log.info("토큰검증");
                decodedToken = firebaseAuth.verifyIdToken(token);

            } catch (Exception e) {
                log.info("유효하지 않은 토큰");
                log.info("에러 : "+e);
                setErrorResponse(response, e);
                return;
            }

            // User를 가져와 SecurityContext에 저장한다.
                UserDetails user = userDetailsService.loadUserByUsername(decodedToken.getUid());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, "", user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean permitUri(HttpServletRequest request){
        if (request.getMethod().equals("GET")){
            for (String patterns : PERMIT_URI) {
                if (request.getRequestURI().startsWith(patterns)) {
                    log.info("허용된 URI");
                    return true;
                }
            }
            log.info("GET - 허용되지 않은 URI");
            return false;
        } else if (request.getRequestURI().startsWith("/api/test/") |
                request.getRequestURI().startsWith("/api/v1/auth/")) {
            log.info("NOT GET - 통과");
            return true;
        } else {
            System.out.println("GET 도 아님 불통");
            return false;
        }
    }

    private void setErrorResponse(HttpServletResponse response, Exception ex) throws IOException {
        log.error("FirebaseTokenFilter Error: "+ex);
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //errorMessage를 담아 보낸다.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), "예외처리 이후 추가");
    }

}
