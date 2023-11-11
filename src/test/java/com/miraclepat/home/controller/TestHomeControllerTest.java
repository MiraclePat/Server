package com.miraclepat.home.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestHomeController.class)
@AutoConfigureRestDocs
class TestHomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 홈_팟_리스트_조회() throws Exception {

        mockMvc.perform(get("/api/test/pats/home?sort=createdBy&category=환경"))
                .andExpect(status().isOk())
                .andDo(document("home",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("몇 개씩 받을지").optional(),
                                parameterWithName("sort").description("정렬 기준-createdTime or nowPerson (필수)"),
                                parameterWithName("search").description("검색어").optional(),
                                parameterWithName("category").description("카테고리")
                        ),
                        responseFields(
                                fieldWithPath("homePatDtoList[].id").type(JsonFieldType.NUMBER).description("팟 Id"),
                                fieldWithPath("homePatDtoList[].repImg").type(JsonFieldType.STRING).description("대표 이미지 URL"),
                                fieldWithPath("homePatDtoList[].patName").type(JsonFieldType.STRING).description("팟 제목"),
                                fieldWithPath("homePatDtoList[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, yyyy-mm-dd"),
                                fieldWithPath("homePatDtoList[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("homePatDtoList[].nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원 수"),
                                fieldWithPath("homePatDtoList[].maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원 수"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총 항목 수")
                        )
                ));


    }
}