package com.miraclepat.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TestAuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureRestDocs
class TestAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 로그인_성공() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("token", "12kk34");

        ResultActions result = mockMvc.perform(post("/api/test/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map)));

        result
                .andExpect(status().isOk())
                .andDo(document("auth-login",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestFields(
                                fieldWithPath("token").description("카카오 액세스 토큰").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("token").type(JsonFieldType.STRING).description("새로 생성한 회원 토큰")
                        )
                ));
    }

    @Test
    void 회원가입_성공() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("userCode", "1234");
        map.put("age", "20대");
        map.put("email", "test@gmail.com");

        ResultActions result = mockMvc.perform(post("/api/test/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map)));

        result.andExpect(status().isCreated())
                .andDo(document("auth-signup",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestFields(
                                fieldWithPath("userCode").description("카카오 유저 고유 code").type(JsonFieldType.STRING),
                                fieldWithPath("age").description("연령대").type(JsonFieldType.STRING),
                                fieldWithPath("email").description("유저 이메일").type(JsonFieldType.STRING)
                        )
                ));
    }

}