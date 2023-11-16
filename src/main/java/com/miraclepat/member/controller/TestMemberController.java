package com.miraclepat.member.controller;

import com.miraclepat.category.entity.Category;
import com.miraclepat.member.dto.MyPatDetailDto;
import com.miraclepat.member.dto.MyPatListDto;
import com.miraclepat.auth.dto.SignupDto;
import com.miraclepat.member.dto.ProfileDto;
import com.miraclepat.auth.dto.TokenDto;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    public ResponseEntity getProfile(){
        ProfileDto ps=new ProfileDto(Constants.REP_IMG,"칸타타",1,3);
        return ResponseEntity.ok(ps);
    }

    //내 정보 업데이트
    @PatchMapping("/me")
    public ResponseEntity updateProfile(
            @RequestPart(name = "image", required = false) MultipartFile image,
            @RequestPart(name = "nickname", required = false) String nickname){

        if (image!=null){
            System.out.println("들어온 이미지 이름 : "+image.getName());
            System.out.println("들어온 이미지 타입 : "+image.getContentType());
            System.out.println("들어온 이미지 진짜 이름 : "+image.getOriginalFilename());
        }

        System.out.println("들어온 닉네임: "+nickname);

        return ResponseEntity.noContent().build();
    }

    //내 알람 정보 업데이트 -> 쿼리 파라미터로 받는다.
    @PatchMapping("/me/push")
    public ResponseEntity updatePush(@RequestParam("push") boolean push){
        System.out.println(push);
        return ResponseEntity.noContent().build();
    }

    //회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity deleteMember(){
        return ResponseEntity.noContent().build();
    }

    //내가 참여한 팟 리스트
    @GetMapping("/pats")
    public ResponseEntity getJoinPatList(
            @RequestParam(name = "lastId", required = false)
            Long lastId,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            int size,
            @RequestParam(name = "sort", required = false)
            String sort,
            @RequestParam(name = "state", required = false)
            String state
    ){
        //내가 참여한 리스트를 보내줘야함
        //id, 타이틀, 카테고리, 주소, 현재 인원, 최대 인원, 시작일, 인증 빈도, 대표 이미지 url

        Category category1 = new Category();
        category1.setCategoryName("환경");

        List<Pat> list = new ArrayList<>();

        for(int i = 0;i<5;i++){
            Pat pat = new Pat();
            pat.setId(Long.valueOf(i));
            pat.setPatDetail("디테일"+i);
            pat.setPatName("내가 참여한, 참여할 챌린지"+i);
            pat.setDays("월,화,수,목,금");
            pat.setCategory(category1);
            pat.setLeader("윈터");
            pat.setEndDate(LocalDate.now().plusDays(i*2));
            pat.setStartDate(LocalDate.now().plusDays(2-i));
            pat.setStartTime(LocalTime.of(10+i, 0));
            pat.setEndTime(LocalTime.of(15+i, 0));
            pat.setPosition(pat.createPoint(37.482778-(0.002*i),126.927592-(0.002*i)));
            pat.setLocation("서울특별시 관악구 신림동");
            pat.setNowPerson(8);
            pat.setMaxPerson(10);
            pat.setRealtime(true);
            pat.setProofDetail("쓰레기 줍고 다니기");
            list.add(pat);
        }

        MyPatListDto myPatListDto = new MyPatListDto(list);

        return ResponseEntity.ok().body(myPatListDto);
    }

    //내가 참여한 팟 상세
    @GetMapping("/pats/{pat-id}")
    public ResponseEntity getJoinPatDetail(){
        //내가 참여한 팟 상세 정보 보여주기
        //내 인증 사진, 다른 사람 인증 사진은 proofController로

        //id, 대표 이미지 url, 타이틀, 시작일, 마감일, 시작 시간, 마감 시간, 인증 요일, 인증 방법, 인증 예시 사진 2종류
        //내 인증 수, 내 최대 인증 수
        //전체 인원 인증 수, 전체 최대 인증 수

        Category category1 = new Category();
        category1.setCategoryName("습관");


        Pat pat = new Pat();
        pat.setId(0L);
        pat.setPatDetail("디테일");
        pat.setPatName("내가 참여한 팟 상세");
        pat.setDays("월,목,금");
        pat.setCategory(category1);
        pat.setLeader("윈터");
        pat.setEndDate(LocalDate.now().plusDays(5));
        pat.setStartDate(LocalDate.now());
        pat.setStartTime(LocalTime.of(10, 0));
        pat.setEndTime(LocalTime.of(15, 0));
        pat.setPosition(pat.createPoint(37.482778,126.927592));
        pat.setLocation("서울특별시 관악구 신림동");
        pat.setNowPerson(8);
        pat.setMaxPerson(10);
        pat.setRealtime(true);
        pat.setProofDetail("쓰레기 줍고 다니기");
        pat.setMaxProof(30);

        MyPatDetailDto myPatDetailDto = new MyPatDetailDto(pat);

        myPatDetailDto.setMyProof(10);
        myPatDetailDto.setAllProof(60);

        return ResponseEntity.ok().body(myPatDetailDto);
    }

    //내가 개설한 팟 리스트 -> 내가 참여한 팟 목록 내용과 같음
    @GetMapping("/pats/open")
    public ResponseEntity getOpenPatList(
            @RequestParam(name = "lastId", required = false)
            Long lastId,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            int size,
            @RequestParam(name = "sort", required = false)
            String sort
    ){
        //내가 개설한 팟 리스트 보여주기

        Category category1 = new Category();
        category1.setCategoryName("습관");

        List<Pat> list = new ArrayList<>();

        for(int i = 0;i<5;i++){
            Pat pat = new Pat();
            pat.setId(Long.valueOf(i));
            pat.setPatDetail("디테일"+i);
            pat.setPatName("내가 개설한 팟 목록"+i);
            pat.setDays("월,화,수");
            pat.setCategory(category1);
            pat.setLeader("윈터");
            pat.setEndDate(LocalDate.now().plusDays(i*2));
            pat.setStartDate(LocalDate.now().plusDays(2-i));
            pat.setStartTime(LocalTime.of(10+i, 0));
            pat.setEndTime(LocalTime.of(15+i, 0));
            pat.setPosition(pat.createPoint(37.482778-(0.002*i),126.927592-(0.002*i)));
            pat.setLocation("서울특별시 관악구 신림동");
            pat.setNowPerson(8);
            pat.setMaxPerson(10);
            pat.setRealtime(true);
            pat.setProofDetail("쓰레기 줍고 다니기");
            list.add(pat);
        }

        MyPatListDto myPatListDto = new MyPatListDto(list);

        return ResponseEntity.ok().body(myPatListDto);

    }

}
