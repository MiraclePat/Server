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
import java.util.Map;

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
        Long memberId = Long.valueOf(principal.getName());
        ProfileDto profileDto = memberService.getProfile(memberId);
        return ResponseEntity.ok(profileDto);
    }

    //내 프로필 사진 업데이트
    @PatchMapping("/me/profile-image")
    public ResponseEntity updateProfileImage(
            @RequestPart(name = "image") MultipartFile image,
            Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        memberService.profileImageUpdate(image, memberId);
        return ResponseEntity.noContent().build();
    }

    //내 닉네임 업데이트
    @PatchMapping("/me/profile-nickname")
    public ResponseEntity updateNickname(
            @RequestBody Map<String, String> nicknameMap,
            Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        String nickname = nicknameMap.get("nickname");
        memberService.nicknameUpdate(nickname, memberId);
        return ResponseEntity.noContent().build();
    }

    //내 알람 정보 업데이트 -> 쿼리 파라미터로 받는다.
    @PatchMapping("/me/push")
    public ResponseEntity updatePush(@RequestParam("push") boolean push, Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        memberService.pushUpdate(push, memberId);
        return ResponseEntity.noContent().build();
    }

    //회원 탈퇴
    //patMember 삭제, proof 삭제,
    @DeleteMapping("/me")
    public ResponseEntity deleteMember(Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    //내가 참여한 팟 리스트
    @GetMapping("/pats")
    public ResponseEntity getJoinPatList(@ModelAttribute MyPatSearchDto myPatSearchDto, Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        MyPatListDto myPatListDto = myPatService.getJoinPatList(myPatSearchDto, memberId);
        return ResponseEntity.ok(myPatListDto);
    }

    //내가 개설한 팟 리스트 -> 내가 참여한 팟 목록 내용과 같음
    @GetMapping("/pats/open")
    public ResponseEntity getOpenPatList(@ModelAttribute MyPatSearchDto myPatSearchDto, Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        MyPatListDto myPatListDto = myPatService.getOpenPatList(myPatSearchDto, memberId);
        return ResponseEntity.ok(myPatListDto);

    }

    //내가 참여한 팟 상세
    @GetMapping("/pats/{pat-id}")
    public ResponseEntity getJoinPatDetail(@PathVariable("pat-id") Long patId, Principal principal) {
        //내 인증 사진, 다른 사람 인증 사진은 proofController로
        Long memberId = Long.valueOf(principal.getName());
        MyPatDetailDto myPatDetailDto = myPatService.getJoinPatDetail(patId, memberId);
        return ResponseEntity.ok(myPatDetailDto);
    }

}
