package com.miraclepat.proof.controller;

import com.miraclepat.home.controller.TestHomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestProofController.class)
@AutoConfigureRestDocs
class TestProofControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 내_인증사진_조회() throws Exception {

        mockMvc.perform(get("/api/test/members/pats/{pat-id}/proofs", 1))
                .andExpect(status().isOk())
                .andDo(document("proof-getMyProof",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("한 페이지당 보낼 항목 수").optional()
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

        mockMvc.perform(get("/api/test/members/pats/{pat-id}/proofs/another", 1))
                .andExpect(status().isOk())
                .andDo(document("proof-getAnotherProof",
                        preprocessRequest(prettyPrint()),   // (2)
                        preprocessResponse(prettyPrint()),  // (3)
                        requestParameters(  //쿼리 파라미터 설명
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("한 페이지당 보낼 항목 수").optional()
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("인증 사진 Id"),
                                fieldWithPath("content[].proofImg").type(JsonFieldType.STRING).description("인증 이미지 URL"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )
                ));

    }
}