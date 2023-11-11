package com.miraclepat.pat.controller;

import com.miraclepat.home.controller.TestHomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestPatController.class)
@AutoConfigureRestDocs
class TestPatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 지도_팟리스트_조회() throws Exception {

        mockMvc.perform(get("/api/test/pats/map?leftLongitude=0.0&rightLongitude=0.0&bottomLatitude=0.0&topLatitude=0.0"))
                .andExpect(status().isOk())
                .andDo(document("pat-getMapPatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("몇 개씩 받을지").optional(),
                                parameterWithName("search").description("검색어").optional(),
                                parameterWithName("category").description("카테고리").optional(),
                                parameterWithName("leftLongitude").description("좌측 경도"),
                                parameterWithName("rightLongitude").description("우측 경도"),
                                parameterWithName("bottomLatitude").description("하단 위도"),
                                parameterWithName("topLatitude").description("상단 위도")
                        ),
                        responseFields(
                                fieldWithPath("patDtoList[].id").type(JsonFieldType.NUMBER).description("팟 Id"),
                                fieldWithPath("patDtoList[].repImg").type(JsonFieldType.STRING).description("대표 이미지 URL"),
                                fieldWithPath("patDtoList[].patName").type(JsonFieldType.STRING).description("팟 제목"),
                                fieldWithPath("patDtoList[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, yyyy-mm-dd"),
                                fieldWithPath("patDtoList[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("patDtoList[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("patDtoList[].longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총 항목 수")
                        )
                ));

    }

    @Test
    void 팟_신청_상세페이지() throws Exception {
        mockMvc.perform(get("/api/test/pats/{pat-id}", 1))
                .andExpect(status().isOk())
                .andDo(document("pat-getPatDetail",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),
                        responseFields(
                                fieldWithPath("patId").type(JsonFieldType.NUMBER).description("팟 ID"),
                                fieldWithPath("repImg").type(JsonFieldType.STRING).description("대표 이미지"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("patName").type(JsonFieldType.STRING).description("팟 이름"),
                                fieldWithPath("location").type(JsonFieldType.STRING).description("위치"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 날짜"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 날짜"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("종료 시간"),
                                fieldWithPath("days").type(JsonFieldType.STRING).description("요일"),
                                fieldWithPath("proofDetail").type(JsonFieldType.STRING).description("인증 상세"),
                                fieldWithPath("bodyImg[]").type(JsonFieldType.ARRAY).description("본문 이미지 URL 리스트"),
                                fieldWithPath("correctImg").type(JsonFieldType.STRING).description("정답 예시 이미지 URL"),
                                fieldWithPath("incorrectImg[]").type(JsonFieldType.ARRAY).description("오답 예시 이미지 URL 리스트"),
                                fieldWithPath("realtime").type(JsonFieldType.STRING).description("실시간 제한 여부"),

                                fieldWithPath("patDetail").type(JsonFieldType.STRING).description("팟 상세 설명"),
                                fieldWithPath("nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원"),
                                fieldWithPath("maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원")
                        )
                ));
    }
}