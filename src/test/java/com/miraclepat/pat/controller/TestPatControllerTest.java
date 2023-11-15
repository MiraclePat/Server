package com.miraclepat.pat.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TestPatController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureRestDocs
class TestPatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 홈_팟_리스트_조회() throws Exception {

        mockMvc.perform(get("/api/test/pats/home?sort=createdBy&category=환경"))
                .andExpect(status().isOk())
                .andDo(document("pat-getHomePatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("마지막 아이템의 id").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("몇 개씩 받을지").optional()
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("10")),
                                parameterWithName("sort").description("정렬 기준").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("HOT / LATEST")),
                                parameterWithName("query").description("검색어").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("쓰레기")),
                                parameterWithName("category").description("카테고리").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("환경")),
                                parameterWithName("showFull").description("인원이 다 찬 방도 보여줄 것인지?").optional()
                                        .attributes(key("타입").value("boolean"),
                                                key("예시").value("true")),
                                parameterWithName("state").description("팟 진행 현황").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("state=SCHEDULED -> 진행 예정인 팟 리스트 조회\n" +
                                                        "state=IN_PROGRESS -> 진행중인 팟 리스트 조회\n" +
                                                        "state=COMPLETED -> 종료된 팟 리스트 조회\n" +
                                                        "null이면 다 조회 가능"))
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("팟 Id"),
                                fieldWithPath("content[].repImg").type(JsonFieldType.STRING).description("대표 이미지 URL"),
                                fieldWithPath("content[].patName").type(JsonFieldType.STRING).description("팟 제목"),
                                fieldWithPath("content[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, yyyy-mm-dd"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("content[].nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원 수"),
                                fieldWithPath("content[].maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원 수"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    void 지도_팟_리스트_조회() throws Exception {

        mockMvc.perform(get("/api/test/pats/map?leftLongitude=0.0&rightLongitude=0.0&bottomLatitude=0.0&topLatitude=0.0"))
                .andExpect(status().isOk())
                .andDo(document("pat-getMapPatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("마지막 아이템의 id").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("몇 개씩 받을지").optional()
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("10")),
                                parameterWithName("query").description("검색어").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("쓰레기")),
                                parameterWithName("category").description("카테고리").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("환경")),
                                parameterWithName("leftLongitude").description("좌측 경도")
                                        .attributes(key("타입").value("double"),
                                        key("예시").value("23.2222222")),
                                parameterWithName("rightLongitude").description("우측 경도")
                                        .attributes(key("타입").value("double"),
                                        key("예시").value("23.2222222")),
                                parameterWithName("bottomLatitude").description("하단 위도")
                                        .attributes(key("타입").value("double"),
                                        key("예시").value("23.2222222")),
                                parameterWithName("topLatitude").description("상단 위도")
                                        .attributes(key("타입").value("double"),
                                        key("예시").value("23.2222222"))
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("팟 Id"),
                                fieldWithPath("content[].repImg").type(JsonFieldType.STRING).description("대표 이미지 URL"),
                                fieldWithPath("content[].patName").type(JsonFieldType.STRING).description("팟 제목"),
                                fieldWithPath("content[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, yyyy-mm-dd"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("content[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("content[].longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));

    }

    @Test
    void 팟_신청_상세페이지() throws Exception {
        objectMapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);

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
                                fieldWithPath("realtime").type(JsonFieldType.BOOLEAN).description("실시간 제한 여부"),

                                fieldWithPath("patDetail").type(JsonFieldType.STRING).description("팟 상세 설명"),
                                fieldWithPath("nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원"),
                                fieldWithPath("maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원"),

                                fieldWithPath("isWriter").type(JsonFieldType.BOOLEAN).description("해당 팟 작성자인지?")
                        )
                ));
    }
}