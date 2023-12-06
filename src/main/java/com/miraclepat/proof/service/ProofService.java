package com.miraclepat.proof.service;

import com.miraclepat.days.repository.DaysRepository;
import com.miraclepat.global.exception.CustomException;
import com.miraclepat.global.exception.ErrorCode;
import com.miraclepat.global.exception.ErrorMessage;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.dto.PatTimeDto;
import com.miraclepat.pat.entity.PatMember;
import com.miraclepat.pat.entity.PatProofInfo;
import com.miraclepat.pat.repository.PatDaysRepository;
import com.miraclepat.pat.repository.PatMemberRepository;
import com.miraclepat.pat.repository.PatProofInfoRepository;
import com.miraclepat.pat.repository.PatRepository;
import com.miraclepat.proof.dto.ProofDto;
import com.miraclepat.proof.dto.ProofListDto;
import com.miraclepat.proof.entity.Proof;
import com.miraclepat.proof.repository.ProofRepository;
import com.miraclepat.utils.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProofService {

    private final PatMemberRepository patMemberRepository;
    private final PatProofInfoRepository patProofInfoRepository;
    private final PatRepository patRepository;
    private final DaysRepository daysRepository;
    private final PatDaysRepository patDaysRepository;
    private final ProofRepository proofRepository;
    private final FileService fileService;

    //인증하기
    @Transactional
    public void proof(Long patId, Long memberId, MultipartFile image) {
        PatMember patMember = patMemberRepository.findByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.FORBIDDEN, ErrorMessage.NOT_JOIN_PAT));
        PatProofInfo patProofInfo = patProofInfoRepository.findByPatId(patId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_PAT));

        validatePatState(patId);
        validateProofDate(patId);
        validateProofTime(patId);
        checkTodayProof(patMember.getId());

        String fileName = fileService.updateFile(image);
        Proof proof = Proof.builder()
                .patMember(patMember)
                .createDate(LocalDate.now())
                .imgName(fileName)
                .oriImgName(image.getOriginalFilename())
                .build();

        patMember.addProofCount();
        patProofInfo.addProofCount();
        proofRepository.save(proof);
    }

    //내 인증 보기
    @Transactional(readOnly = true)
    public ProofListDto getMyProof(Long lastId, int size, Long patId, Long memberId) {
        //1.patmember id 값을 찾는다.(patid와 내 memberId)
        Long patMemberId = patMemberRepository.findIdByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.FORBIDDEN, ErrorMessage.NOT_JOIN_PAT));
        //2.proofRepository에서 찾는다.
        ProofListDto proofListDto = proofRepository.getMyProof(lastId, size, patMemberId);
        //3.url 설정
        for (ProofDto proofDto : proofListDto.getContent()) {
            proofDto.setProofImg(fileService.getUrl(proofDto.getProofImg()));
        }
        return proofListDto;
    }

    //다른 사람 인증 보기
    @Transactional(readOnly = true)
    public ProofListDto getAnotherProof(Long lastId, int size, Long patId, Long memberId) {
        //1.patmember id 값을 찾는다.
        Long patMemberId = patMemberRepository.findIdByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.FORBIDDEN, ErrorMessage.NOT_JOIN_PAT));
        List<Long> patMemberIds = patMemberRepository.findIdsByPatId(patId);

        //내 인증 이미지는 제외한다.
        patMemberIds.remove(patMemberId);

        //2.proofRepository에서 찾는다.
        ProofListDto proofListDto = proofRepository.getAnotherProof(lastId, size, patMemberIds);
        //3.url 설정
        for (ProofDto proofDto : proofListDto.getContent()) {
            proofDto.setProofImg(fileService.getUrl(proofDto.getProofImg()));
        }
        return proofListDto;
    }

    //팟 상태가 진행중인지 검증
    private void validatePatState(Long patId) {
        State state = patRepository.findStateByPatId(patId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_PAT));
        if (state != State.IN_PROGRESS) {
            throw new IllegalStateException(ErrorMessage.NOT_IN_PROGRESS);
        }
    }

    //오늘이 인증 요일인지 검증
    private void validateProofDate(Long patId) {
        LocalDate today = LocalDate.now();
        String dayOfWeek = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        Long dayId = daysRepository.findIdByDayName(dayOfWeek);
        if (!patDaysRepository.existsByPatIdAndDaysId(patId, dayId)) {
            throw new IllegalStateException(ErrorMessage.NOT_PROOF_DAY);
        }
    }

    //인증 시간 검증 -현재 인증 시간이 startTime과 endTime 사이에 있는지 검사한다.
    private void validateProofTime(Long patId) {
        LocalTime nowTime = LocalTime.now();
        PatTimeDto patTimeDto = patRepository.getPatTimes(patId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXIST_PAT));

        //endTime이 자정인 경우 startTime 으로만 판단
        if (patTimeDto.getEndTime() == LocalTime.MIDNIGHT) {
            if (nowTime.isBefore(patTimeDto.getStarTime())) {
                throw new IllegalStateException(ErrorMessage.NOT_PROOF_TIME);
            }
        } else {
            if (nowTime.isBefore(patTimeDto.getStarTime()) || nowTime.isAfter(patTimeDto.getEndTime())) {
                throw new IllegalStateException(ErrorMessage.NOT_PROOF_TIME);
            }
        }
    }

    //이미 인증을 했는지 검증
    private void checkTodayProof(Long patMemberId) {
        LocalDate today = LocalDate.now();
        //이미 인증한 이력이 있는지 검사한다.
        boolean exists = proofRepository.existsTodayProof(patMemberId, today);
        if (exists) {
            throw new IllegalStateException(ErrorMessage.EXIST_TODAY_PROOF);
        }
    }
}
