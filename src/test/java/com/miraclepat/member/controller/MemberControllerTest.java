package com.miraclepat.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miraclepat.member.dto.*;
import com.miraclepat.member.service.MemberService;
import com.miraclepat.member.service.MyPatService;
import com.miraclepat.pat.constant.ButtonState;
import com.miraclepat.pat.constant.ImgType;
import com.miraclepat.pat.constant.State;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MemberController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureRestDocs
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @MockBean
    MyPatService myPatService;

    private Principal mockPrincipal;

    @BeforeEach
    void setup() {
        mockPrincipal = Mockito.mock(Principal.class);
        given(mockPrincipal.getName()).willReturn("1");
    }

    @Test
    void 프로필_조회() throws Exception {

        ProfileDto profileDto = new ProfileDto("프로필 img url", "닉네임", 3, 1);
        given(memberService.getProfile(Mockito.any(Long.class)))
                .willReturn(profileDto);

        mockMvc.perform(get("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(document("member-getProfile",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        responseFields(
                                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("finPats").type(JsonFieldType.NUMBER).description("완료된 팟의 수"),
                                fieldWithPath("openPats").type(JsonFieldType.NUMBER).description("내가 개설한 팟의 수")
                        )
                ));
    }

    @Test
    void 내_프로필_이미지_업데이트() throws Exception {

        MockMultipartFile image = new MockMultipartFile("image", "filename-1.jpeg", "image/jpeg", "image".getBytes());
        doNothing().when(memberService).profileImageUpdate(Mockito.any(MultipartFile.class), Mockito.any(Long.class));

        MockMultipartHttpServletRequestBuilder builder =
                RestDocumentationRequestBuilders.
                        multipart("/api/v1/members/me/profile-image");

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        ResultActions result = mockMvc.perform(builder
                .file(image)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal));

        result.andExpect(status().isNoContent())
                .andDo(document("member-updateProfileImage",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        requestParts(
                                partWithName("image").description("이 필드는 MultipartFile 타입의 이미지 파일을 받습니다.")
                        )
                ));
    }

    @Test
    void 내_프로필_닉네임_업데이트() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("nickname", "변경닉네임");

        doNothing().when(memberService).nicknameUpdate(Mockito.any(String.class), Mockito.any(Long.class));

        ResultActions result = mockMvc.perform(patch("/api/v1/members/me/profile-nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal));

        result.andExpect(status().isNoContent())
                .andDo(document("member-updateProfileNickname",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("변경 닉네임").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    void 알람_정보_업데이트() throws Exception {

        doNothing().when(memberService).pushUpdate(Mockito.any(boolean.class), Mockito.any(Long.class));

        ResultActions result = mockMvc.perform(patch("/api/v1/members/me/push?push=true")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal));

        result.andExpect(status().isNoContent())
                .andDo(document("member-updatePush",
                                preprocessRequest(prettyPrint()),   // (2)
                                preprocessResponse(prettyPrint()),  // (3),,
                                requestHeaders(
                                        headerWithName("Authorization").description("유효한 토큰")
                                ),
                                requestParameters(
                                        parameterWithName("push").description("알림을 받을지 말지. 받는다 true 안받는다 false").optional()
                                                .attributes(key("타입").value("boolean"),
                                                        key("예시").value("true"))
                                )
                        )
                );

    }

    @Test
    void 회원_탈퇴() throws Exception {

        doNothing().when(memberService).deleteMember(Mockito.any(Long.class));

        ResultActions result = mockMvc.perform(delete("/api/v1/members/me")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal));

        result
                .andExpect(status().isNoContent())
                .andDo(document("member-deleteMember",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        )
                ));
    }

    @Test
    void 참여한_진행예정_or_진행중인_or_완료한_팟_리스트_조회() throws Exception {

        MyPatListDto myPatListDto = new MyPatListDto(getMyPatDtoList(), true);
        given(myPatService.getJoinPatList(Mockito.any(MyPatSearchDto.class), Mockito.any(Long.class)))
                .willReturn(myPatListDto);

        ResultActions result = mockMvc.perform(get("/api/v1/members/pats")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal));

        result
                .andExpect(status().isOk())
                .andDo(document("member-getJoinPatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("페이지 번호").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("한 페이지당 보낼 항목 수").optional()
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("1")),
                                parameterWithName("state").description("내가 참여한 팟 진행 현황").optional()
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
                                fieldWithPath("content[].location").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("content[].days").type(JsonFieldType.STRING).description("참여 가능 요일"),
                                fieldWithPath("content[].state").type(JsonFieldType.STRING).description("팟 진행 상태: SCHEDULED(진행 예정), IN_PROGRESS(진행중), COMPLETED(완료)"),
                                fieldWithPath("content[].isCompleted").type(JsonFieldType.BOOLEAN).description("당일 인증 데이터 있으면 true 없으면 false"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    void 내가_개설한_팟_리스트_조회() throws Exception {

        MyPatListDto myPatListDto = new MyPatListDto(getMyPatDtoList(), true);
        given(myPatService.getOpenPatList(Mockito.any(MyPatSearchDto.class), Mockito.any(Long.class)))
                .willReturn(myPatListDto);

        mockMvc.perform(get("/api/v1/members/pats/open?lastId=1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(document("member-getOpenPatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("페이지 번호").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("한 페이지당 보낼 항목 수")
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("1")).optional(),
                                parameterWithName("state").description("내가 참여한 팟 진행 현황").optional()
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
                                fieldWithPath("content[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜 M월 d일"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("content[].nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원 수"),
                                fieldWithPath("content[].maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원 수"),
                                fieldWithPath("content[].location").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("content[].days").type(JsonFieldType.STRING).description("참여 가능 요일: '월, 수, 금'"),
                                fieldWithPath("content[].state").type(JsonFieldType.STRING).description("팟 진행 상태: SCHEDULED(진행 예정), IN_PROGRESS(진행중), COMPLETED(완료)"),
                                fieldWithPath("content[].isCompleted").type(JsonFieldType.BOOLEAN).description("당일 인증 데이터 있으면 true 없으면 false"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    void 참여한_팟_상세조회() throws Exception {

        MyPatDetailDto myPatDetailDto = getMyPatDetailDto();
        given(myPatService.getJoinPatDetail(Mockito.any(Long.class), Mockito.any(Long.class)))
                .willReturn(myPatDetailDto);

        mockMvc.perform(get("/api/v1/members/pats/{pat-id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(document("member-getJoinPatDetail",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("pat-id").description("id")
                        ),
                        responseFields(
                                fieldWithPath("patId").type(JsonFieldType.NUMBER).description("팟 ID"),
                                fieldWithPath("repImg").type(JsonFieldType.STRING).description("대표 이미지"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("patName").type(JsonFieldType.STRING).description("팟 이름"),
                                fieldWithPath("location").type(JsonFieldType.STRING).description("위치"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 날짜: m월 d일"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 날짜: m월 d일"),
                                fieldWithPath("modifiedStartDate").type(JsonFieldType.STRING).description("시작 날짜: m월 d일(요일)"),
                                fieldWithPath("modifiedEndDate").type(JsonFieldType.STRING).description("종료 날짜:  m월 d일(요일)"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("종료 시간"),
                                fieldWithPath("days").type(JsonFieldType.STRING).description("요일: '월, 화'"),
                                fieldWithPath("dayList[]").type(JsonFieldType.ARRAY).description("요일: ['월요일', '화요일']"),
                                fieldWithPath("patDetail").type(JsonFieldType.STRING).description("팟 내용 상세"),
                                fieldWithPath("proofDetail").type(JsonFieldType.STRING).description("인증 상세"),
                                fieldWithPath("bodyImg[]").type(JsonFieldType.ARRAY).description("본문 이미지 URL 리스트"),
                                fieldWithPath("correctImg").type(JsonFieldType.STRING).description("정답 예시 이미지 URL"),
                                fieldWithPath("incorrectImg").type(JsonFieldType.STRING).description("오답 예시 이미지 URL"),
                                fieldWithPath("realtime").type(JsonFieldType.BOOLEAN).description("실시간 제한 여부"),
                                fieldWithPath("maxProof").type(JsonFieldType.NUMBER).description("최대 인증 수"),
                                fieldWithPath("myProof").type(JsonFieldType.NUMBER).description("내 인증 수"),
                                fieldWithPath("allProof").type(JsonFieldType.NUMBER).description("전체 인증 수"),
                                fieldWithPath("allMaxProof").type(JsonFieldType.NUMBER).description("전체 최대 인증 수"),
                                fieldWithPath("myFailProof").type(JsonFieldType.NUMBER).description("나의 실패한 인증 수"),
                                fieldWithPath("allFailProof").type(JsonFieldType.NUMBER).description("전체 실패한 인증 수"),
                                fieldWithPath("state").type(JsonFieldType.STRING).description("버튼 상태 설명: CANCELABLE(취소가능), NO_CANCELABLE(취소불가), IN_PROGRESS(진행중), COMPLETED(완료)"),
                                fieldWithPath("isCompleted").type(JsonFieldType.BOOLEAN).description("당일 인증 유무: true(있음), false(없음)")
                        )
                ));
    }

    List<MyPatDto> getMyPatDtoList() {
        List<MyPatDto> content = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            LocalDate date = LocalDate.now();
            State state = State.SCHEDULED;
            content.add(new MyPatDto((long) i, "대표이미지 url", "팟 이름입니다.", date, "기타",
                    3, 10, "강남", "월, 수, 금", state, false));
        }
        return content;
    }

    MyPatDetailDto getMyPatDetailDto() {

        List<String> dayList = new ArrayList<>();
        dayList.add("월요일");
        dayList.add("금요일");

        MyPatDetailDto myPatDetailDto = MyPatDetailDto.builder()
                .patId(1L)
                .category("기타")
                .patName("팟 상세 이름입니다.")
                .location("")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .modifiedStartDate(LocalDate.now())
                .modifiedEndDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(1))
                .days("월, 금")
                .dayList(dayList)
                .patDetail("팟 상세 페이지 디테일 내용입니다.")
                .proofDetail("팟 상세 페이지 인증 디테일 내용입니다.")
                .realtime(true)
                .maxProof(2)
                .myProof(1)
                .allProof(4)
                .allMaxProof(4)
                .myFailProof(1)
                .allFailProof(1)
                .completed(true)
                .build();

        myPatDetailDto.setState(ButtonState.CANCELABLE);
        myPatDetailDto.setImg("대표 이미지 url", ImgType.REPRESENTATIVE);
        myPatDetailDto.setImg("옳은 예시 이미지 url", ImgType.CORRECT);
        myPatDetailDto.setImg("틀린 예시 이미지 url", ImgType.INCORRECT);
        myPatDetailDto.setImg("본문 이미지 url", ImgType.BODY);

        return myPatDetailDto;
    }

}