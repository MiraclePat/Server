package com.miraclepat.member.service;

import com.miraclepat.global.exception.CustomException;
import com.miraclepat.global.exception.ErrorCode;
import com.miraclepat.global.exception.ErrorMessage;
import com.miraclepat.member.dto.ProfileDto;
import com.miraclepat.member.entity.Member;
import com.miraclepat.member.repository.MemberRepository;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.pat.entity.PatMember;
import com.miraclepat.pat.repository.PatMemberRepository;
import com.miraclepat.pat.repository.PatRepository;
import com.miraclepat.security.FirebaseAuthHelper;
import com.miraclepat.utils.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    // 내 프로필 조회, 내 프로필 업데이트, 알람 정보업데이트

    private final MemberRepository memberRepository;
    private final PatRepository patRepository;
    private final PatMemberRepository patMemberRepository;
    private final FileService fileService;
    private final FirebaseAuthHelper firebaseAuthHelper;

    //프로필 조회
    @Transactional(readOnly = true)
    public ProfileDto getProfile(Long memberId) {
        //닉네임, 프로필사진, 완료한 팟 수, 개설한 팟 수
        ProfileDto profileDto = memberRepository.findNicknameAndProfileImgById(memberId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_MEMBER_INFO));
        profileDto.setProfileImg(getProfileImgUrl(profileDto.getProfileImg()));

        //완료한 팟 수 -> patMember에서 memberId로 참여한 팟 id 먼저 받기 -> 팟에서 state:Completed인거 count
        List<Long> ids = patMemberRepository.findJoinPatIdsByMemberId(memberId);
        int finPats = patRepository.countByState(ids, State.COMPLETED);

        //개설한 팟 수 -> patRepository에서 memberId가 memberId인거 찾기
        int openPats = patRepository.countByMemberId(memberId);
        profileDto.setPats(finPats, openPats);

        return profileDto;
    }

    //프로필 업데이트
    @Transactional
    public void profileImageUpdate(MultipartFile image, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_MEMBER_INFO));

        if (image != null && !image.isEmpty()) {
            //현재 프로필 이미지가 존재한다면 삭제한다.
            String nowImg = member.getProfileImg();

            if (nowImg != null) {
                fileService.deleteFile(nowImg);
            }

            String fileName = fileService.updateFile(image);
            member.updateProfileImg(fileName);
        }else {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_FILE);
        }
    }

    //닉네임 업데이트
    @Transactional
    public void nicknameUpdate(String nickname, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_MEMBER_INFO));

        if (!member.getNickname().equals(nickname) && memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.EXIST_RESOURCE, ErrorMessage.EXIST_NICKNAME);
        }
        member.setNickname(nickname);
    }

    //알람 업데이트
    @Transactional
    public void pushUpdate(boolean push, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_MEMBER_INFO));
        member.updatePush(push);
    }

    //회원 탈퇴
    @Transactional
    public void deleteMember(Long memberId) {
        //firebase 탈퇴
        String userCode = memberRepository.findUserCodeById(memberId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_MEMBER_INFO));
        firebaseAuthHelper.deleteUser(userCode);

        //pat의 작성자를 null로 변경
        List<Long> ids = patRepository.findOpenPatIdsByMemberId(memberId);
        for (Long patId : ids) {
            Pat pat = patRepository.findById(patId)
                    .orElseThrow();
            pat.deleteMember();
        }

        List<Long> patMemberIds = patMemberRepository.findIdsByMemberId(memberId);
        for (Long id : patMemberIds) {
            PatMember patMember = patMemberRepository.findById(id)
                    .orElseThrow();
            patMember.deleteMember();
        }

        //member 삭제
        memberRepository.deleteById(memberId);
    }

    private String getProfileImgUrl(String profileImg) {
        // profileImg가 null이거나 빈 문자열일 경우 "마스코트"로 설정
        if (profileImg == null || profileImg.isEmpty()) {
            return fileService.getUrl("MiraclePat_mascot.jpg");
        } else {
            return fileService.getUrl(profileImg);
        }
    }
}
