package com.miraclepat.pat.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miraclepat.pat.constant.ButtonState;
import com.miraclepat.pat.dto.*;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.pat.entity.PatDays;
import com.miraclepat.pat.service.PatImgService;
import com.miraclepat.pat.service.PatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
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

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = PatController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureRestDocs
class PatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PatService patService;

    @MockBean
    PatImgService patImgService;

    private Principal mockPrincipal;

    @BeforeEach
    void setup() {
        mockPrincipal = Mockito.mock(Principal.class);
        given(mockPrincipal.getName()).willReturn("1");
    }

    @Test
    void 홈_배너() throws Exception {

        HomeBannerDto homeBannerDto = new HomeBannerDto(1L, "배너 팟이름", LocalDate.now());
        given(patService.getHomeBanner(Mockito.any(Long.class)))
                .willReturn(homeBannerDto);

        mockMvc.perform(get("/api/v1/pats/home/banner")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(document("pat-getHomeBanner",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰/있으면 전송, 없으면 생략").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("팟 Id/넘겨줄게 없다면 null"),
                                fieldWithPath("patName").type(JsonFieldType.STRING).description("팟 제목/넘겨줄게 없다면 null"),
                                fieldWithPath("date").type(JsonFieldType.STRING).description("D+며칠/넘겨줄게 없다면 null")
                        )
                ));
    }

    @Test
    void 홈_팟_리스트_조회() throws Exception {

        HomePatListDto homePatListDto = new HomePatListDto(getHomePatDtoList(), false);
        given(patService.getHomePatList(Mockito.any(HomePatSearchDto.class)))
                .willReturn(homePatListDto);

        mockMvc.perform(get("/api/v1/pats/home?category=환경"))
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
                                                key("예시").value("HOT / LATEST / RANDOM")),
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
                                fieldWithPath("content[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, M.d(E)"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("content[].location").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("content[].nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원 수"),
                                fieldWithPath("content[].maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원 수"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    void 지도_팟_리스트_조회() throws Exception {

        PatListDto patListDto = new PatListDto(getPatDtoList());
        given(patService.getMapPatList(Mockito.any(MapPatSearchDto.class)))
                .willReturn(patListDto);

        mockMvc.perform(get("/api/v1/pats/map?leftLongitude=0.0&rightLongitude=0.0&bottomLatitude=0.0&topLatitude=0.0"))
                .andExpect(status().isOk())
                .andDo(document("pat-getMapPatList",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("size").description("화면에 출력할 팟 수").optional()
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("10")),
                                parameterWithName("query").description("검색어").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("쓰레기")),
                                parameterWithName("category").description("카테고리").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("환경")),
                                parameterWithName("state").description("팟 진행 상태").optional()
                                        .attributes(key("타입").value("String"),
                                                key("예시").value("IN_PROGRESS")),
                                parameterWithName("showFull").description("인원이 다 찬 방도 보여줄지?").optional()
                                        .attributes(key("타입").value("boolean"),
                                                key("예시").value("true")),
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
                                fieldWithPath("content[].startDate").type(JsonFieldType.STRING).description("팟 시작 날짜, M월 d일(E)"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("content[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("content[].longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("content[].nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원"),
                                fieldWithPath("content[].maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원"),
                                fieldWithPath("content[].days").type(JsonFieldType.STRING).description("인증 요일"),
                                fieldWithPath("content[].location").type(JsonFieldType.STRING).description("주소")
                        )
                ));

    }

    @Test
    void 팟_생성() throws Exception {

        List<List<String>> imgInfoList = new ArrayList<>();
        given(patImgService.uploadPatImg(Mockito.any(MultipartFile.class), Mockito.any(MultipartFile.class), Mockito.any(MultipartFile.class), Mockito.any(List.class)))
                .willReturn(imgInfoList);
        doNothing().when(patService).createPat(Mockito.any(CreatePatDto.class), Mockito.any(List.class), Mockito.any(Long.class));

        objectMapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);

        MockMultipartFile repImg = new MockMultipartFile("repImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());
        MockMultipartFile correctImg = new MockMultipartFile("correctImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());
        MockMultipartFile incorrectImg = new MockMultipartFile("incorrectImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());
        MockMultipartFile bodyImg = new MockMultipartFile("bodyImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());
        MockMultipartFile bodyImg2 = new MockMultipartFile("bodyImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());

        CreatePatDto createPatDto = 팟생성();

        String createPatDtoJson = objectMapper.writeValueAsString(createPatDto);
        MockMultipartFile pat = new MockMultipartFile("pat", "", "application/json", createPatDtoJson.getBytes(StandardCharsets.UTF_8));

        ResultActions result = mockMvc.perform(multipart("/api/v1/pats")
                .file(repImg)
                .file(correctImg)
                .file(incorrectImg)
                .file(bodyImg)
                .file(bodyImg2)
                .file(pat)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal));

        result.andExpect(status().isCreated())
                .andDo(document("pat-createPat",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        requestParts(
                                partWithName("repImg").description("이 필드는 MultipartFile 타입의 대표 이미지 파일을 받습니다."),
                                partWithName("correctImg").description("이 필드는 MultipartFile 타입의 옳은 예시 이미지 파일을 받습니다."),
                                partWithName("incorrectImg").description("이 필드는 MultipartFile 타입의 틀린 예시 이미지 파일을 받습니다."),
                                partWithName("bodyImg").description("이 필드는 MultipartFile 타입의 본문 이미지 파일 리스트를 받습니다."),
                                partWithName("pat").description("팟 설정 내용")
                        )
                ));

    }

    @Test
    void 팟_상세페이지() throws Exception {

        PatDetailDto detailDto = getPatDetailDto();

        given(patService.detailPat(Mockito.any(Long.class), Mockito.any(Long.class)))
                .willReturn(detailDto);

        mockMvc.perform(get("/api/v1/pats/{pat-id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(document("pat-getPatDetail",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰/있으면 전송, 없으면 생략").optional()
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
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 날짜: yyyy-MM-dd"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 날짜: yyyy-MM-dd"),
                                fieldWithPath("modifiedStartDate").type(JsonFieldType.STRING).description("시작 날짜: m월 d일(요일)"),
                                fieldWithPath("modifiedEndDate").type(JsonFieldType.STRING).description("종료 날짜:  m월 d일(요일)"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("종료 시간"),
                                fieldWithPath("dayList[]").type(JsonFieldType.ARRAY).description("요일"),
                                fieldWithPath("proofDetail").type(JsonFieldType.STRING).description("인증 상세"),
                                fieldWithPath("bodyImg[]").type(JsonFieldType.ARRAY).description("본문 이미지 URL 리스트"),
                                fieldWithPath("correctImg").type(JsonFieldType.STRING).description("정답 예시 이미지 URL"),
                                fieldWithPath("incorrectImg").type(JsonFieldType.STRING).description("오답 예시 이미지 URL"),
                                fieldWithPath("realtime").type(JsonFieldType.BOOLEAN).description("실시간 제한 여부"),
                                fieldWithPath("patDetail").type(JsonFieldType.STRING).description("팟 상세 설명"),
                                fieldWithPath("nowPerson").type(JsonFieldType.NUMBER).description("현재 참여 인원"),
                                fieldWithPath("maxPerson").type(JsonFieldType.NUMBER).description("최대 참여 인원"),
                                fieldWithPath("isWriter").type(JsonFieldType.BOOLEAN).description("해당 팟 작성자인지?"),
                                fieldWithPath("isJoiner").type(JsonFieldType.BOOLEAN).description("해당 팟 참여자인지?"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("작성자 프로필 이미지"),
                                fieldWithPath("state").type(JsonFieldType.STRING).description("버튼 상태 설명: CANCELABLE(취소가능), NO_CANCELABLE(취소불가), IN_PROGRESS(진행중), COMPLETED(완료)")
                        )
                ));
    }

    //참여하기
    @Test
    void 팟_참여하기() throws Exception {

        doNothing().when(patService).joinPat(Mockito.any(Long.class), Mockito.any(Long.class));

        ResultActions result = mockMvc.perform(post("/api/v1/pats/{pat-id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal));

        result.andExpect(status().isCreated())
                .andDo(document("pat-join",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("pat-id").description("id")
                        )
                ));
    }

    //수정하기
    @Test
    void 팟_내용_수정() throws Exception {

        List<List<String>> imgInfoList = new ArrayList<>();
        given(patImgService.uploadPatImg(Mockito.any(MultipartFile.class), Mockito.any(MultipartFile.class), Mockito.any(MultipartFile.class), Mockito.any(List.class)))
                .willReturn(imgInfoList);
        doNothing().when(patService).createPat(Mockito.any(CreatePatDto.class), Mockito.any(List.class), Mockito.any(Long.class));

        MockMultipartFile file1 = new MockMultipartFile("image", "filename-1.jpeg", "image/jpeg", "some text".getBytes());
        MockMultipartFile nickname = new MockMultipartFile("nickname", "", "text/plain", "닉네임".getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                RestDocumentationRequestBuilders.
                        multipart("/api/v1/pats/{pat-id}", 1);

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        MockMultipartFile repImg = new MockMultipartFile("repImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());
        MockMultipartFile correctImg = new MockMultipartFile("correctImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());
        MockMultipartFile incorrectImg = new MockMultipartFile("incorrectImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());
        MockMultipartFile bodyImg = new MockMultipartFile("bodyImg", "filename-1.jpeg", "image/jpeg", "some text".getBytes());

        CreatePatDto createPatDto = 팟생성();

        String createPatDtoJson = objectMapper.writeValueAsString(createPatDto);
        MockMultipartFile pat = new MockMultipartFile("pat", "", "application/json", createPatDtoJson.getBytes(StandardCharsets.UTF_8));

        ResultActions result = mockMvc.perform(builder
                .file(repImg)
                .file(correctImg)
                .file(incorrectImg)
                .file(bodyImg)
                .file(pat)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal));

        result.andExpect(status().isNoContent())
                .andDo(document("pat-updatePat",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("pat-id").description("id")
                        ),
                        requestParts(
                                partWithName("repImg").description("이 필드는 MultipartFile 타입의 대표 이미지 파일을 받습니다."),
                                partWithName("correctImg").description("이 필드는 MultipartFile 타입의 옳은 예시 이미지 파일을 받습니다."),
                                partWithName("incorrectImg").description("이 필드는 MultipartFile 타입의 틀린 예시 이미지 파일을 받습니다."),
                                partWithName("bodyImg").description("이 필드는 MultipartFile 타입의 본문 이미지 파일 리스트를 받습니다."),
                                partWithName("pat").description("팟 상세. 필드명 request 예시 참조")
                        )
                ));

    }

    //팟 삭제
    @Test
    void 팟_삭제() throws Exception {

        doNothing().when(patService).deletePat(Mockito.any(Long.class), Mockito.any(Long.class));

        ResultActions result = mockMvc.perform(delete("/api/v1/pats/{pat-id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal)
        );

        result.andExpect(status().isNoContent())
                .andDo(document("pat-delete",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("pat-id").description("id")
                        )
                ));
    }

    //팟 가입 신청 취소
    @Test
    void 팟_가입_취소() throws Exception {

        doNothing().when(patService).withdrawPat(Mockito.any(Long.class), Mockito.any(Long.class));

        ResultActions result = mockMvc.perform(delete("/api/v1/pats/{pat-id}/withdraw", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal)
        );

        result.andExpect(status().isNoContent())
                .andDo(document("pat-withdraw",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("pat-id").description("id")
                        )
                ));

    }

    CreatePatDto 팟생성() {

        CreatePatDto createPatDto = new CreatePatDto();
        createPatDto.setPatName("팟 이름");
        createPatDto.setPatDetail("팟 상세 내용 이런 너무 짧아");
        createPatDto.setMaxPerson(10);
        createPatDto.setLatitude(37.5665);
        createPatDto.setLongitude(126.9780);
        createPatDto.setLocation("서울특별시 중구 세종대로 110");
        createPatDto.setCategory("카테고리");
        createPatDto.setStartTime(LocalTime.parse("10:00"));
        createPatDto.setEndTime(LocalTime.parse("18:00"));
        createPatDto.setStartDate(LocalDate.parse("2023-01-01"));
        createPatDto.setEndDate(LocalDate.parse("2023-12-31"));
        createPatDto.setProofDetail("인증 방법 상세");
        List<String> day = new ArrayList<>();
        day.add("월요일");
        day.add("수요일");
        day.add("금요일");
        createPatDto.setDays(day);
        createPatDto.setRealtime(true);

        return createPatDto;
    }

    PatDetailDto getPatDetailDto() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);

        List<String> dayList = new ArrayList<>();
        dayList.add("월요일");
        dayList.add("화요일");

        List<String> bodyImg = new ArrayList<>();
        dayList.add("본문 이미지 url");

        PatDetailDto patDetailDto = new PatDetailDto(
                1L, "대표이미지 url", "기타", "팟 디테일 팟이름", "강남",
                startDate, endDate, startDate, endDate, LocalTime.now(), LocalTime.MIDNIGHT, dayList,
                "인증 디테일입니다.", "옳은 예시 이미지 url", "틀린 예시 이미지 url", bodyImg, true,
                "팟 디테일 설명입니다.", 3, 10, true, true,
                "도전자01", "작성자 프로필 이미지 url", ButtonState.NO_CANCELABLE);

        return patDetailDto;
    }

    List<HomePatDto> getHomePatDtoList() {
        List<HomePatDto> content = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            content.add(new HomePatDto((long) i, "대표이미지 url", "팟 이름입니다.", LocalDate.now(),
                    "기타", "강남", 3, 10));
        }
        return content;
    }

    List<PatDto> getPatDtoList() {
        List<PatDto> content = new ArrayList<>();

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(127.2, 37.2));

        for (int i = 0; i < 3; i++) {
            content.add(new PatDto((long) i, "대표이미지 url", "팟 이름입니다.", LocalDate.now(),
                    "기타", point, 3, 10, "월, 수, 금", "강남"));
        }
        return content;
    }
}