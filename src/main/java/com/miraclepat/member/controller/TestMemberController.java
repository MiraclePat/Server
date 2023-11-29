package com.miraclepat.member.controller;

import com.miraclepat.category.entity.Category;
import com.miraclepat.member.dto.*;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.dto.HomePatDto;
import com.miraclepat.pat.dto.HomePatListDto;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/test/members")
public class TestMemberController {


    //내 정보 가져오기
    @GetMapping("/me")
    public ResponseEntity getProfile() {
        ProfileDto ps = new ProfileDto(Constants.REP_IMG, "칸타타", 1, 3);
        return ResponseEntity.ok(ps);
    }

    //내 정보 업데이트
    @PatchMapping("/me")
    public ResponseEntity updateProfile(
            @RequestPart(name = "image", required = false) MultipartFile image,
            @RequestPart(name = "nickname", required = false) String nickname) {

        if (image != null) {
            System.out.println("들어온 이미지 이름 : " + image.getName());
            System.out.println("들어온 이미지 타입 : " + image.getContentType());
            System.out.println("들어온 이미지 진짜 이름 : " + image.getOriginalFilename());
        }

        System.out.println("들어온 닉네임: " + nickname);

        return ResponseEntity.noContent().build();
    }

    //내 알람 정보 업데이트 -> 쿼리 파라미터로 받는다.
    @PatchMapping("/me/push")
    public ResponseEntity updatePush(@RequestParam("push") boolean push) {
        System.out.println(push);
        return ResponseEntity.noContent().build();
    }

    //회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity deleteMember() {
        return ResponseEntity.noContent().build();
    }

    //내가 참여한 팟 리스트
    @GetMapping("/pats")
    public ResponseEntity getJoinPatList(@ModelAttribute MyPatSearchDto myPatSearchDto) {
        //내가 참여한 리스트를 보내줘야함
        //id, 타이틀, 카테고리, 주소, 현재 인원, 최대 인원, 시작일, 인증 빈도, 대표 이미지 url

        Category category1 = new Category(1L, "취미");
        List<State> s = new ArrayList<>();
        s.add(State.SCHEDULED);
        s.add(State.SCHEDULED);
        s.add(State.IN_PROGRESS);
        s.add(State.IN_PROGRESS);
        s.add(State.COMPLETED);

        List<MyPatDto> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Pat pat = Pat.builder()
                    .id(Long.valueOf(i))
                    .patDetail("팟 디테일 내용입니다. 15자-500자")
                    .patName("참여한 팟 리스트입니다.")
                    .days("월, 금")
                    .category(category1)
                    .endDate(LocalDate.now().plusDays(2))
                    .endTime(LocalTime.now().plusHours(2))
                    .startDate(LocalDate.now().plusDays(1))
                    .startTime(LocalTime.now())
                    .location("서울특별시 관악구 신림동")
                    .nowPerson(3)
                    .maxPerson(12)
                    .realtime(true)
                    .state(s.get(i))
                    .proofDetail("길거리에서 쓰레기 줍고 다니기")
                    .build();
            list.add(new MyPatDto(pat));
        }

        MyPatListDto myPatListDto = new MyPatListDto(list, true);
        return ResponseEntity.ok().body(myPatListDto);
    }

    //내가 참여한 팟 상세
    @GetMapping("/pats/{pat-id}")
    public ResponseEntity getJoinPatDetail(@ModelAttribute MyPatSearchDto myPatSearchDto) {
        //내가 참여한 팟 상세 정보 보여주기
        //내 인증 사진, 다른 사람 인증 사진은 proofController로

        //id, 대표 이미지 url, 타이틀, 시작일, 마감일, 시작 시간, 마감 시간, 인증 요일, 인증 방법, 인증 예시 사진 2종류
        //내 인증 수, 내 최대 인증 수
        //전체 인원 인증 수, 전체 최대 인증 수

        Category category1 = new Category(1L, "기타");

            Pat pat = Pat.builder()
                    .id(1L)
                    .patDetail("팟 디테일 내용입니다. 15자-500자")
                    .patName("참여한 팟 리스트입니다.")
                    .days("월, 금")
                    .category(category1)
                    .endDate(LocalDate.now().plusDays(2))
                    .endTime(LocalTime.now().plusHours(2))
                    .startDate(LocalDate.now().plusDays(1))
                    .startTime(LocalTime.now())
                    .location("서울특별시 관악구 신림동")
                    .nowPerson(3)
                    .maxPerson(12)
                    .realtime(true)
                    .proofDetail("길거리에서 쓰레기 줍고 다니기")
                    .state(State.SCHEDULED)
                    .build();

        MyPatDetailDto myPatDetailDto = new MyPatDetailDto(pat);

        return ResponseEntity.ok().body(myPatDetailDto);
    }

    //내가 개설한 팟 리스트 -> 내가 참여한 팟 목록 내용과 같음
    @GetMapping("/pats/open")
    public ResponseEntity getOpenPatList(@ModelAttribute MyPatSearchDto myPatSearchDto) {
        //내가 개설한 팟 리스트 보여주기

        Category category1 = new Category(1L, "건강");

        List<MyPatDto> list = new ArrayList<>();
        List<State> s = new ArrayList<>();
        s.add(State.SCHEDULED);
        s.add(State.SCHEDULED);
        s.add(State.IN_PROGRESS);
        s.add(State.IN_PROGRESS);
        s.add(State.COMPLETED);

        for (int i = 0; i < 5; i++) {
            Pat pat = Pat.builder()
                    .id(1L)
                    .patDetail("팟 디테일 내용입니다. 15자-500자")
                    .patName("참여한 팟 리스트입니다."+i)
                    .days("월, 금")
                    .category(category1)
                    .endDate(LocalDate.now().plusDays(2))
                    .endTime(LocalTime.now().plusHours(2))
                    .startDate(LocalDate.now().plusDays(1))
                    .startTime(LocalTime.now())
                    .location("서울특별시 관악구 신림동")
                    .nowPerson(3)
                    .maxPerson(12)
                    .realtime(true)
                    .proofDetail("길거리에서 쓰레기 줍고 다니기")
                    .state(s.get(i))
                    .build();
            list.add(new MyPatDto(pat));
        }

        MyPatListDto myPatListDto = new MyPatListDto(list, false);

        return ResponseEntity.ok().body(myPatListDto);

    }

}