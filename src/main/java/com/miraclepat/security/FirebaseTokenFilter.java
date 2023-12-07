package com.miraclepat.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miraclepat.global.exception.ErrorCode;
import com.miraclepat.global.exception.ErrorResponse;
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
    public static List<String> PERMIT_URI = Arrays.asList(
            "/api/v1/auth", "/api/v1/pats/home", "/api/v1/pats/map",
            "/api/test/", "/docs/");
    private static final String BEARER = "Bearer ";

    private UserDetailsServiceImpl userDetailsService;
    private FirebaseAuthHelper firebaseAuthHelper;

    public FirebaseTokenFilter(UserDetailsServiceImpl userDetailsService, FirebaseAuthHelper firebaseAuthHelper) {
        this.userDetailsService = userDetailsService;
        this.firebaseAuthHelper = firebaseAuthHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!permitUri(request)) {
            //팟 상세 페이지와 배너를 로그인 한 상태로 보면 토큰 검증
            String requestUri = request.getRequestURI();
            boolean isGetMethod = request.getMethod().equals("GET");
            boolean isAuthHeaderEmpty = request.getHeader(HttpHeaders.AUTHORIZATION) == null;
            boolean isStartsWithPatsOrBanner = requestUri.startsWith("/api/v1/pats/") || requestUri.startsWith("/api/v1/home/banner");

            if (!(isGetMethod && isAuthHeaderEmpty && isStartsWithPatsOrBanner)) {
                try {
                    String firebaseToken = request.getHeader(HttpHeaders.AUTHORIZATION);
                    String idToken = getIdTokenByFirebaseToken(firebaseToken);
                    String userCode = firebaseAuthHelper.getUid(idToken);
                    UserDetails user = userDetailsService.loadUserByUsername(userCode);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (RuntimeException ex) {
                    setErrorResponse(response, ex);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean permitUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.equals("/api/v1/home/banner")) {
            log.info("허용되지 않은 URI");
            return false;
        }
        boolean isPermitUri = PERMIT_URI.stream().anyMatch(uri::startsWith);
        if (isPermitUri) {
            log.info("허용된 URI");
        } else {
            log.info("허용되지 않은 URI");
        }
        return isPermitUri;
    }

    private String getIdTokenByFirebaseToken(String firebaseToken) {
        if (!firebaseToken.startsWith(BEARER)) {
            throw new IllegalArgumentException(firebaseToken + ": Bearer 형식의 토큰이 아닙니다");
        }
        return firebaseToken.split(" ")[1];
    }

    private void setErrorResponse(HttpServletResponse response, Exception ex) throws IOException {
        log.error("[FirebaseTokenFilter]: " + ex);
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //errorMessage를 담아 보낸다.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), errorResponse());
    }

    private ErrorResponse errorResponse() {
        return ErrorResponse.of(ErrorCode.INVALID_FIREBASE_ID_TOKEN,
                ErrorCode.INVALID_FIREBASE_ID_TOKEN.getMessage());
    }
}
