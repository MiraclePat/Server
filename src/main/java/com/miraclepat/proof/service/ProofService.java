package com.miraclepat.proof.service;

import com.miraclepat.days.repository.DaysRepository;
import com.miraclepat.pat.constant.State;
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
    public void proof(Long patId, Long memberId, MultipartFile image) throws Exception{
        PatMember patMember = patMemberRepository.findByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new NoSuchElementException("팟에 가입된 회원이 아닙니다."));
        PatProofInfo patProofInfo = patProofInfoRepository.findByPatId(patId)
                .orElseThrow(() -> new NoSuchElementException("팟 info를 찾을 수 없습니다."));

        validatePatState(patId);
        validateProofDate(patId);

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
    public ProofListDto getMyProof(Long lastId, int size, Long patId, Long memberId){
        //1.patmember id 값을 찾는다.(patid와 내 memberId)
        Long patMemberId = patMemberRepository.findIdByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new NoSuchElementException("팟 가입 정보를 찾을 수 없습니다."));
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
    public ProofListDto getAnotherProof(Long lastId, int size, Long patId, Long memberId){
        //1.patmember id 값을 찾는다.
        Long patMemberId = patMemberRepository.findIdByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new NoSuchElementException("팟 가입 정보를 찾을 수 없습니다."));
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

    private void validatePatState(Long patId){
        State state = patRepository.findStateByPatId(patId)
                .orElseThrow(() -> new NoSuchElementException("팟 상태를 확인해주세요."));
        if (state != State.IN_PROGRESS) {
            throw new IllegalStateException("인증일이 아닙니다.");
        }
    }

    private void validateProofDate(Long patId){
        LocalDate today = LocalDate.now();
        String dayOfWeek = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        Long dayId = daysRepository.findIdByDayName(dayOfWeek);
        if (!patDaysRepository.existsByPatIdAndDaysId(patId, dayId)) {
            throw new IllegalStateException("시작일이 아닙니다.");
        }
    }
}
