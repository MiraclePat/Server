package com.miraclepat.member.service;

import com.miraclepat.member.dto.MyPatDetailDto;
import com.miraclepat.member.dto.MyPatDto;
import com.miraclepat.member.dto.MyPatListDto;
import com.miraclepat.member.dto.MyPatSearchDto;
import com.miraclepat.pat.constant.ButtonState;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.pat.entity.PatImg;
import com.miraclepat.pat.entity.PatMember;
import com.miraclepat.pat.entity.PatProofInfo;
import com.miraclepat.pat.repository.PatMemberRepository;
import com.miraclepat.pat.repository.PatProofInfoRepository;
import com.miraclepat.pat.repository.PatRepository;
import com.miraclepat.proof.repository.ProofRepository;
import com.miraclepat.utils.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPatService {
    // 내가 참여한 팟 리스트(참여예정, 참여중, 완료), 참여한 팟 상세, 개설한 팟 리스트

    private final PatRepository patRepository;
    private final PatProofInfoRepository patProofInfoRepository;
    private final PatMemberRepository patMemberRepository;
    private final ProofRepository proofRepository;
    private final FileService fileService;

    //내가 참여한 팟 리스트 가져오기
    @Transactional(readOnly = true)
    public MyPatListDto getJoinPatList(MyPatSearchDto myPatSearchDto, Long memberId){
        //내가 참여한 팟 id 리스트 가져오기
        List<Long> ids = patMemberRepository.findJoinPatIdsByMemberId(memberId);
        if (!ids.isEmpty()){
            MyPatListDto myPatListDto = patRepository.getJoinPatList(ids, myPatSearchDto.getLastId(),
                    myPatSearchDto.getSize(), myPatSearchDto.getState());

            //팟 리스트에서 당일 인증 정보가 있는지 검사하기: state = IN_PROGRESS 일 때만
            //인증 정보가 있다면 isCompleted를 true로 변경한다. (기본 false)
            if (myPatSearchDto.getState() == State.IN_PROGRESS){
                for (MyPatDto myPatDto : myPatListDto.getContent()) {
                    myPatDto.setCompleted(checkTodayProof(myPatDto.getId(), memberId));
                }
            }
            setRepImgUrl(myPatListDto);
            return myPatListDto;
        }
        return new MyPatListDto(new ArrayList<>(), false);
    }

    //내가 오픈한 팟 리스트 가져오기
    @Transactional(readOnly = true)
    public MyPatListDto getOpenPatList(MyPatSearchDto myPatSearchDto, Long memberId){
        List<Long> ids = patRepository.findOpenPatIdsByMemberId(memberId);

        if (!ids.isEmpty()){
            MyPatListDto myPatListDto = patRepository.getOpenPatList(ids, myPatSearchDto.getLastId(),
                    myPatSearchDto.getSize(), myPatSearchDto.getState());

            for (MyPatDto myPatDto : myPatListDto.getContent()) {
                if (myPatDto.getState() == State.IN_PROGRESS){
                    myPatDto.setCompleted(checkTodayProof(myPatDto.getId(), memberId));
                }
            }
            setRepImgUrl(myPatListDto);
            return myPatListDto;
        }
        return new MyPatListDto(new ArrayList<>(), false);
    }

    //내가 참여한 팟 상세(인증탭)
    @Transactional(readOnly = true)
    public MyPatDetailDto getJoinPatDetail(Long patId, Long memberId){
        Pat pat = patRepository.findById(patId)
                .orElseThrow(() -> new NoSuchElementException("팟 정보를 찾을 수 없습니다."));
        PatProofInfo patProofInfo = patProofInfoRepository.findByPatId(patId)
                .orElseThrow(() -> new NoSuchElementException("팟 info를 찾을 수 없습니다."));
        PatMember patMember = patMemberRepository.findByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new NoSuchElementException("팟 가입 정보를 찾을 수 없습니다."));

        List<String> dayList = pat.getPatDaysList().stream()
                .map(patDays -> patDays.getDays().getDayName())
                .collect(Collectors.toList());
        int todayMaxProof = countTodayMaxProof(dayList, pat.getStartDate());

        int onePersonMaxProof = patProofInfo.getMaxProof();

        MyPatDetailDto myPatDetailDto = MyPatDetailDto.builder()
                .patId(patId)
                .category(pat.getCategory().getCategoryName())
                .patName(pat.getPatName())
                .location(pat.getLocation())
                .startDate(pat.getStartDate())
                .endDate(pat.getEndDate())
                .modifiedStartDate(pat.getStartDate())
                .modifiedEndDate(pat.getEndDate())
                .startTime(pat.getStartTime())
                .endTime(pat.getEndTime())
                .days(pat.getDays())
                .dayList(dayList)
                .patDetail(pat.getPatDetail())
                .proofDetail(pat.getProofDetail())
                .realtime(pat.isRealtime())
                .maxProof(onePersonMaxProof)
                .myProof(patMember.getProofCount())
                .allProof(patProofInfo.getProofCount())
                .allMaxProof(onePersonMaxProof * pat.getNowPerson())
                .myFailProof(todayMaxProof - patMember.getProofCount())
                .allFailProof((todayMaxProof * pat.getNowPerson()) - patProofInfo.getProofCount())
                .completed(checkTodayProof(patId, memberId))
                .build();

        //ButtonState 설정
        switch (pat.getState()){
            case SCHEDULED -> {
                try{
                    validateOneDayDifference(pat.getStartDate());
                    myPatDetailDto.setState(ButtonState.CANCELABLE);
                }catch (Exception e){
                    myPatDetailDto.setState(ButtonState.NO_CANCELABLE);
                }
            }
            case IN_PROGRESS -> myPatDetailDto.setState(ButtonState.IN_PROGRESS);
            case COMPLETED -> myPatDetailDto.setState(ButtonState.COMPLETED);
        }

        //이미지 url 적용
        for (PatImg patImg : pat.getPatImgList()) {
            myPatDetailDto.setImg(fileService.getUrl(patImg.getImgName()), patImg.getImgType());}

        return myPatDetailDto;
    }

    //진행중인 팟의 당일 인증 여부를 확인
    private boolean checkTodayProof(Long patId, Long memberId){
        LocalDate today = LocalDate.now();
        Long id = patMemberRepository.findIdByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new NoSuchElementException("참여 정보를 찾을 수 없습니다."));
        return proofRepository.existsTodayProof(id, today);
    }

    //repImg의 url을 설정
    private void setRepImgUrl(MyPatListDto myPatListDto){
        for (MyPatDto myPatDto : myPatListDto.getContent()) {
            myPatDto.setRepImg(fileService.getUrl(myPatDto.getRepImg()));
            System.out.println("***********:"+myPatDto.getRepImg());
        }
    }

    //조회 당일 기준으로 최대 인증 수
    private int countTodayMaxProof(List<String> days, LocalDate startDate){
        long daysBetween = ChronoUnit.DAYS.between(startDate, LocalDate.now())+1;  // 두 날짜 사이의 일 수
        // 정해진 기간 동안 days에 포함된 요일 수를 계산
        int count = (int) IntStream.range(0, (int) daysBetween)
                .mapToObj(startDate::plusDays)
                .filter(date -> days.contains(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN)))
                .count();

        return count;
    }

    private void validateOneDayDifference(LocalDate startDate){
        long days = ChronoUnit.DAYS.between(LocalDate.now(), startDate);
        if (days <= 1) {
            throw new IllegalStateException("하루 전부터는 수정이 불가능합니다.");
        }
    }
}
