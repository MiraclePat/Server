package com.miraclepat.member.controller;

import com.miraclepat.member.dto.MyPatDetailDto;
import com.miraclepat.member.dto.MyPatListDto;
import com.miraclepat.member.dto.MyPatSearchDto;
import com.miraclepat.member.dto.ProfileDto;
import com.miraclepat.member.service.MemberService;
import com.miraclepat.member.service.MyPatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Validated
@RestController
@RequestMapping("/api/v1/members")
@AllArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MyPatService myPatService;

    //내 정보 가져오기
    @GetMapping("/me")
    public ResponseEntity getProfile(Principal principal) {
        ProfileDto profileDto = memberService.getProfile(1L);
        return ResponseEntity.ok(profileDto);
    }

    //내 정보 업데이트
    @PatchMapping("/me")
    public ResponseEntity updateProfile(
            @RequestPart(name = "image", required = false) MultipartFile image,
            @RequestPart(name = "nickname") String nickname,
            Principal principal) {
        memberService.profileUpdate(image, nickname, 1L);
        return ResponseEntity.noContent().build();
    }

    //내 알람 정보 업데이트 -> 쿼리 파라미터로 받는다.
    @PatchMapping("/me/push")
    public ResponseEntity updatePush(@RequestParam("push") boolean push, Principal principal) {
        memberService.pushUpdate(push, 1L);
        return ResponseEntity.noContent().build();
    }

    //회원 탈퇴
    //patMember 삭제, proof 삭제,
    @DeleteMapping("/delete")
    public ResponseEntity deleteMember(Principal principal) {
        memberService.deleteMember(1L);
        return ResponseEntity.noContent().build();
    }

    //내가 참여한 팟 리스트
    @GetMapping("/pats")
    public ResponseEntity getJoinPatList(@ModelAttribute MyPatSearchDto myPatSearchDto, Principal principal) {
        MyPatListDto myPatListDto = myPatService.getJoinPatList(myPatSearchDto, 1L);
        return ResponseEntity.ok(myPatListDto);
    }

    //내가 개설한 팟 리스트 -> 내가 참여한 팟 목록 내용과 같음
    @GetMapping("/pats/open")
    public ResponseEntity getOpenPatList(@ModelAttribute MyPatSearchDto myPatSearchDto, Principal principal) {
        MyPatListDto myPatListDto = myPatService.getOpenPatList(myPatSearchDto, 1L);
        return ResponseEntity.ok(myPatListDto);

    }

    //내가 참여한 팟 상세
    @GetMapping("/pats/{pat-id}")
    public ResponseEntity getJoinPatDetail(@PathVariable("pat-id") Long patId, Principal principal) {
        //내 인증 사진, 다른 사람 인증 사진은 proofController로
        MyPatDetailDto myPatDetailDto = myPatService.getJoinPatDetail(patId, 1L);
        return ResponseEntity.ok(myPatDetailDto);
    }

}
