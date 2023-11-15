package com.miraclepat.member.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.access.SecurityConfig;
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

@WebMvcTest(value = TestMemberController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureRestDocs
class TestMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 프로필_조회() throws Exception {
        mockMvc.perform(get("/api/test/members/me"))
                .andExpect(status().isOk())
                .andDo(document("member-getProfile",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),
                        responseFields(
                                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("finPats").type(JsonFieldType.NUMBER).description("완료된 팟의 수"),
                                fieldWithPath("openPats").type(JsonFieldType.NUMBER).description("내가 개설한 팟의 수")
                        )
                ));
    }

    @Test
    void 참여한_팟_리스트_조회() throws Exception {
        mockMvc.perform(get("/api/test/members/pats?lastId=1"))
                .andExpect(status().isOk())
                .andDo(document("member-getJoinPatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("페이지 번호").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("한 페이지당 보낼 항목 수").optional()
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("1")),
                                parameterWithName("sort").description("정렬 조건").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("혹시 몰라서 넣었습니다."))
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("팟 Id"),
                                fieldWithPath("content[].repImg").type(JsonFieldType.STRING).description("대표 이미지 URL"),
                                fieldWithPath("content[].patName").type(JsonFieldType.STRING).description("팟 제목"),
                                fieldWithPath("content[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, yyyy-mm-dd"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("content[].nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원 수"),
                                fieldWithPath("content[].maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원 수"),
                                fieldWithPath("content[].location").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("content[].days").type(JsonFieldType.STRING).description("참여 가능 요일"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    void 내가_개설한_팟_리스트_조회() throws Exception {
        mockMvc.perform(get("/api/test/members/pats/open?lastId=1"))
                .andExpect(status().isOk())
                .andDo(document("member-getOpenPatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("페이지 번호").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("한 페이지당 보낼 항목 수")
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("1")).optional(),
                                parameterWithName("sort").description("정렬 조건").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("혹시 몰라서 넣었습니다."))
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("팟 Id"),
                                fieldWithPath("content[].repImg").type(JsonFieldType.STRING).description("대표 이미지 URL"),
                                fieldWithPath("content[].patName").type(JsonFieldType.STRING).description("팟 제목"),
                                fieldWithPath("content[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, yyyy-mm-dd"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("content[].nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원 수"),
                                fieldWithPath("content[].maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원 수"),
                                fieldWithPath("content[].location").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("content[].days").type(JsonFieldType.STRING).description("참여 가능 요일"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    void 종료한_팟_리스트_조회() throws Exception {
        mockMvc.perform(get("/api/test/members/pats/finish?lastId=1"))
                .andExpect(status().isOk())
                .andDo(document("member-getFinishPatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("페이지 번호").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("한 페이지당 보낼 항목 수").optional()
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("1")),
                                parameterWithName("sort").description("정렬 조건")
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("혹시 몰라서 넣었습니다.")).optional()
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("팟 Id"),
                                fieldWithPath("content[].repImg").type(JsonFieldType.STRING).description("대표 이미지 URL"),
                                fieldWithPath("content[].patName").type(JsonFieldType.STRING).description("팟 제목"),
                                fieldWithPath("content[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, yyyy-mm-dd"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("content[].nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원 수"),
                                fieldWithPath("content[].maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원 수"),
                                fieldWithPath("content[].location").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("content[].days").type(JsonFieldType.STRING).description("참여 가능 요일"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    void 참여한_팟_상세조회() throws Exception {
        mockMvc.perform(get("/api/test/members/pats/{pat-id}", 1))
                .andExpect(status().isOk())
                .andDo(document("member-getJoinPatDetail",
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
                                fieldWithPath("maxProof").type(JsonFieldType.NUMBER).description("최대 인증 수"),
                                fieldWithPath("myProof").type(JsonFieldType.NUMBER).description("내 인증 수"),
                                fieldWithPath("allProof").type(JsonFieldType.NUMBER).description("전체 인증 수"),
                                fieldWithPath("allMaxProof").type(JsonFieldType.NUMBER).description("전체 최대 인증 수"),
                                fieldWithPath("myFailProof").type(JsonFieldType.NUMBER).description("나의 실패한 인증 수"),
                                fieldWithPath("allFailProof").type(JsonFieldType.NUMBER).description("전체 실패한 인증 수")
                        )
                ));
    }

}