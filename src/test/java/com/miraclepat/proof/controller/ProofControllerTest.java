package com.miraclepat.proof.controller;

import com.miraclepat.proof.dto.ProofDto;
import com.miraclepat.proof.dto.ProofListDto;
import com.miraclepat.proof.service.ProofService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProofController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureRestDocs
class ProofControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProofService proofService;

    private Principal mockPrincipal;

    @BeforeEach
    void setup() {
        mockPrincipal = Mockito.mock(Principal.class);
        given(mockPrincipal.getName()).willReturn("1");
    }

    @Test
    void 인증하기() throws Exception {

        doNothing().when(proofService).proof(Mockito.any(Long.class), Mockito.any(Long.class), Mockito.any(MultipartFile.class));

        MockMultipartFile proofImg = new MockMultipartFile("proofImg", "filename-1.txt", "image/png", "some text".getBytes());

        ResultActions result = mockMvc.perform(multipart("/api/v1/members/pats/{pat-id}/proofs", 1)
                .file(proofImg)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal)
        );

        result.andExpect(status().isCreated())
                .andDo(document("proof-proof",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3),,
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("pat-id").description("인증하려는 팟 id")
                        ),
                        requestParts(
                                partWithName("proofImg").description("이 필드는 MultipartFile 타입의 인증 이미지 파일을 받습니다.")
                        )
                ));
    }

    @Test
    void 내_인증사진_조회() throws Exception {

        ProofListDto proofListDto = new ProofListDto(getProofDtoList(), true);
        given(proofService.getMyProof(Mockito.any(Long.class), Mockito.any(int.class), Mockito.any(Long.class), Mockito.any(Long.class)))
                .willReturn(proofListDto);

        ResultActions result = mockMvc.perform(get("/api/v1/members/pats/{pat-id}/proofs?lastId=2", 1)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal)
        );

        result
                .andExpect(status().isOk())
                .andDo(document("proof-getMyProof",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("pat-id").description("인증하려는 팟 id")
                        ),
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("마지막 아이템의 id").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("몇 개씩 받을지").optional()
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("10"))
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("인증 사진 Id"),
                                fieldWithPath("content[].proofImg").type(JsonFieldType.STRING).description("인증 이미지 URL"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));

    }

    @Test
    void 다른사람_인증사진_조회() throws Exception {

        ProofListDto proofListDto = new ProofListDto(getProofDtoList(), false);
        given(proofService.getAnotherProof(Mockito.any(Long.class), Mockito.any(int.class), Mockito.any(Long.class), Mockito.any(Long.class)))
                .willReturn(proofListDto);

        ResultActions result = mockMvc.perform(get("/api/v1/members/pats/{pat-id}/proofs/another?lastId=2", 1)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testToken")
                .principal(mockPrincipal)
        );

        result
                .andExpect(status().isOk())
                .andDo(document("proof-getAnotherProof",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("pat-id").description("인증하려는 팟 id")
                        ),
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("lastId").description("마지막 아이템의 id").optional()
                                        .attributes(key("타입").value("Long"),
                                                key("예시").value("1")),
                                parameterWithName("size").description("몇 개씩 받을지").optional()
                                        .attributes(key("타입").value("int"),
                                                key("예시").value("10"))
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("인증 사진 Id"),
                                fieldWithPath("content[].proofImg").type(JsonFieldType.STRING).description("인증 이미지 URL"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));
    }

    List<ProofDto> getProofDtoList() {
        List<ProofDto> content = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            content.add(new ProofDto((long) i, "인증 이미지 url"));
        }

        return content;
    }
}