package com.miraclepat.pat.service;

import com.miraclepat.category.entity.Category;
import com.miraclepat.category.repository.CategoryRepository;
import com.miraclepat.days.repository.DaysRepository;
import com.miraclepat.member.entity.Member;
import com.miraclepat.member.repository.MemberRepository;
import com.miraclepat.pat.constant.ButtonState;
import com.miraclepat.pat.constant.ImgType;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.dto.*;
import com.miraclepat.pat.entity.*;
import com.miraclepat.pat.repository.*;
import com.miraclepat.proof.repository.ProofRepository;
import com.miraclepat.utils.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatService {

    private final PatRepository patRepository;
    private final PatProofInfoRepository patProofInfoRepository;
    private final CategoryRepository categoryRepository;
    private final DaysRepository daysRepository;
    private final PatDaysRepository patDaysRepository;
    private final PatImgRepository patImgRepository;
    private final PatMemberRepository patMemberRepository;
    private final MemberRepository memberRepository;
    private final ProofRepository proofRepository;
    private final FileService fileService;

    //팟 생성
    @Transactional
    public void createPat(
            CreatePatDto createPatDto,
            List<List<String>> imgInfoList,
            Long memberId
    ) {

        validateTwoDayDifference(createPatDto.getStartDate(), "시작일은 오늘로부터 최소 이틀 이후여야 합니다.");
        validateStartDateAndEndDate(createPatDto.getStartDate(), createPatDto.getEndDate());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
        Category category = categoryRepository.findByCategoryName(createPatDto.getCategory());

        //팟 생성
        Pat pat = Pat.builder()
                .member(member)
                .category(category)
                .patName(createPatDto.getPatName())
                .patDetail(createPatDto.getPatDetail())
                .proofDetail(createPatDto.getProofDetail())
                .repImg(imgInfoList.get(0).get(0))
                .realtime(createPatDto.isRealtime())
                .maxPerson(createPatDto.getMaxPerson())
                .startTime(createPatDto.getStartTime())
                .endTime(createPatDto.getEndTime())
                .startDate(createPatDto.getStartDate())
                .endDate(createPatDto.getEndDate())
                .state(State.SCHEDULED)
                .build();

        //주소와 좌표가 있다면 설정
        if (createPatDto.getLongitude() != null && createPatDto.getLatitude() != null
                && !createPatDto.getLocation().equals("")
        ) {
            Point point = createPoint(createPatDto.getLongitude(), createPatDto.getLatitude());
            pat.setLocationAndPoint(createPatDto.getLocation(), point);
        }
        pat.setDays(createDays(createPatDto.getDays()));
        log.info("이미지 리스트를 설정합니다.");
        pat.setPatImgList(createPatImg(imgInfoList, pat));
        pat.setPatDaysList(createPatDays(createPatDto.getDays(), pat));

        //리더는 자동으로 참여
        PatMember patMember = new PatMember(pat, member);
        patMemberRepository.save(patMember);
        pat.addPerson();

        pat.updateTime();
        patRepository.save(pat);

        //인증 수 정보를 담은 patProofInfo 생성
        int maxProof = countMaxProof(createPatDto.getDays(), createPatDto.getStartDate(), createPatDto.getEndDate());
        PatProofInfo patProofInfo = new PatProofInfo(maxProof, pat);
        patProofInfoRepository.save(patProofInfo);
    }

    //팟 수정하기 pat, patMember, patImg, patDays, patProof
    @Transactional
    public void updatePat(
            CreatePatDto createPatDto,
            List<List<String>> imgInfoList,
            Long memberId, Long patId) throws Exception {
        Pat pat = patRepository.findById(patId)
                .orElseThrow(() -> new IllegalArgumentException("팟 정보를 찾을 수 없습니다."));
        Long writerId = patRepository.findMemberIdByPatId(patId)
                .orElseThrow(() -> new UsernameNotFoundException("작성자를 찾을 수 없습니다."));
        PatProofInfo patProofInfo = patProofInfoRepository.findByPatId(patId)
                .orElseThrow(() -> new NoSuchElementException("팟 인증 정보를 찾을 수 없습니다."));

        //조건 검증
        validateMemberPermission(writerId, memberId);
        validateTwoDayDifference(pat.getStartDate(), "시작일 하루 전부터는 수정이 불가능합니다.");
        validateStartDateAndEndDate(createPatDto.getStartDate(), createPatDto.getEndDate());
        validateMaxPersonChange(pat.getNowPerson(), createPatDto.getMaxPerson());

        //삭제 예정 파일을 담아둔다.
        List<String> deleteFileName = pat.getPatImgList().stream()
                .map(PatImg::getImgName)
                .collect(Collectors.toList());
        List<Long> deletePatDays = patDaysRepository.findIdsByPatId(patId);
        List<Long> deletePatImg = patImgRepository.findIdsByPatId(patId);

        Category category = categoryRepository.findByCategoryName(createPatDto.getCategory());
        pat.updatePat(createPatDto, category, imgInfoList.get(0).get(0));

        //주소와 좌표가 있다면 설정
        if (createPatDto.getLongitude() != null && createPatDto.getLatitude() != null
                && !createPatDto.getLocation().equals("")
        ) {
            Point point = createPoint(createPatDto.getLongitude(), createPatDto.getLatitude());
            pat.setLocationAndPoint(createPatDto.getLocation(), point);
        }
        pat.setDays(createDays(createPatDto.getDays()));
        pat.setPatImgList(createPatImg(imgInfoList, pat));
        pat.setPatDaysList(createPatDays(createPatDto.getDays(), pat));

        //기존 이미지, 요일 삭제
        deleteFileName.stream().forEach(imgName -> fileService.deleteFile(imgName));
        patImgRepository.deleteAllByIdInBatch(deletePatImg);
        patDaysRepository.deleteAllByIdInBatch(deletePatDays);

        int maxProof = countMaxProof(createPatDto.getDays(), createPatDto.getStartDate(), createPatDto.getEndDate());
        patProofInfo.setMaxProof(maxProof);

        pat.updateTime();
        patRepository.save(pat);
    }

    //팟 삭제. pat, patMember, patImg, patDays, patProof
    @Transactional
    public void deletePat(Long patId, Long memberId) throws Exception {
        Pat pat = patRepository.findById(patId)
                .orElseThrow(() -> new IllegalArgumentException("팟 정보를 찾을 수 없습니다."));

        Long writerId = patRepository.findMemberIdByPatId(patId).orElseThrow(() -> new UsernameNotFoundException("작성자를 찾을 수 없습니다."));
        validateMemberPermission(writerId, memberId);
        validateTwoDayDifference(pat.getStartDate(), "시작일 하루 전부터는 삭제가 불가능합니다.");

        //patMember도 삭제한다. -> 참여자가 있으면 삭제 못하게도 할 수 있을 것 같다.
        List<Long> ids = patMemberRepository.findIdsByPatId(patId);
        patMemberRepository.deleteAllById(ids);

        //팟의 인증 정보 삭제
        Long id = patProofInfoRepository.findIdByPatId(patId);
        patProofInfoRepository.deleteById(id);
        pat.getPatImgList().stream().forEach(patImg -> fileService.deleteFile(patImg.getImgName()));
        patRepository.delete(pat);
    }

    //참여하기
    @Transactional
    public void joinPat(Long patId, Long memberId) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
        Pat pat = patRepository.findById(patId)
                .orElseThrow(() -> new IllegalArgumentException("팟 정보를 찾을 수 없습니다."));

        validateJoinPatDate(patId);

        if (!patMemberRepository.existsByPatIdAndMemberId(patId, memberId)) {
            if (pat.addPerson()) {
                PatMember patMember = new PatMember(pat, member);
                patMemberRepository.save(patMember);
            } else {
                throw new IllegalArgumentException("최대 인원입니다.");
            }
        } else {
            throw new IllegalArgumentException("이미 팟에 참여하셨습니다.");
        }
    }

    //참여 취소
    @Transactional
    public void withdrawPat(Long patId, Long memberId) throws Exception {
        Pat pat = patRepository.findById(patId)
                .orElseThrow(() -> new IllegalArgumentException("팟을 찾을 수 없습니다."));
        Long writerId = patRepository.findMemberIdByPatId(patId)
                .orElseThrow(() -> new UsernameNotFoundException("작성자를 찾을 수 없습니다."));
        Long id = patMemberRepository.findIdByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("참여중이 아닙니다."));

        validateTwoDayDifference(pat.getStartDate(), "시작일 하루 전부터는 참여 취소가 불가능합니다.");

        if (writerId != memberId) {
            pat.subPerson();
            patMemberRepository.deleteById(id);
        } else {
            //리더는 탈퇴할 수 없음.
            throw new IllegalArgumentException("리더는 탈퇴 불가능합니다.");
        }
    }

    //상세보기
    @Transactional(readOnly = true)
    public PatDetailDto detailPat(Long patId, Long memberId) throws Exception {
        Pat pat = patRepository.findById(patId)
                .orElseThrow(() -> new IllegalArgumentException("팟 정보를 찾을 수 없습니다."));
        WriterInfoDto writerInfoDto = patRepository.findMemberInfoByPatId(patId)
                .orElse(new WriterInfoDto(0L, "탈퇴한 회원입니다.", "MiraclePat_mascot.jpg")); //id, nickname, profile

        //프로필이 없다면 마스코트를 보여줌
        if (writerInfoDto.getProfileImg() == null || writerInfoDto.getProfileImg().isEmpty()) {
            writerInfoDto.setProfileImg("MiraclePat_mascot.jpg");
        }

        PatDetailDto patDetailDto = new PatDetailDto();
        patDetailDto.setPat(pat);
        patDetailDto.setWriterProfile(writerInfoDto.getNickname(),
                fileService.getUrl(writerInfoDto.getProfileImg()));

        for (PatImg patImg : pat.getPatImgList()) {
            patDetailDto.setImg(fileService.getUrl(patImg.getImgName()), patImg.getImgType());
        }

        if (memberId != null) { //로그인 한 사용자가 접근했을 경우
            boolean isWriter = (writerInfoDto.getId() == memberId);
            boolean isJoiner = patMemberRepository.existsByPatIdAndMemberId(patId, memberId);
            patDetailDto.setIsWriterAndIsJoiner(isWriter, isJoiner);
        }

        //팟 State에 따른 ButtonState 설정
        switch (pat.getState()) {
            case SCHEDULED -> {
                try {
                    validateTwoDayDifference(pat.getStartDate(), "");
                    patDetailDto.setState(ButtonState.CANCELABLE);
                } catch (Exception e) {
                    patDetailDto.setState(ButtonState.NO_CANCELABLE);
                }
            }
            case IN_PROGRESS -> patDetailDto.setState(ButtonState.IN_PROGRESS);
            case COMPLETED -> patDetailDto.setState(ButtonState.COMPLETED);
        }

        return patDetailDto;
    }

    //홈 배너
    @Transactional(readOnly = true)
    public HomeBannerDto getHomeBanner(Long memberId) {
        HomeBannerDto homeBannerDto = new HomeBannerDto();
        List<Long> patIds = patMemberRepository.findJoinPatIdsByMemberId(memberId);
        if (patIds.isEmpty()) {
            return homeBannerDto;
        }

        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        long dayOfWeekValue = dayOfWeek.getValue();
        List<HomeBannerDto> bannerDtoList = patRepository.getTodayProofPatList(patIds, dayOfWeekValue);

        //인증 내역이 없어야한다.
        bannerDtoList.removeIf(dto -> isTodayProof(dto.getId(), memberId));
        if (bannerDtoList.isEmpty()) {
            return homeBannerDto;
        }
        //랜덤
        homeBannerDto = bannerDtoList.get(ThreadLocalRandom.current().nextInt(bannerDtoList.size()));

        return homeBannerDto;
    }

    //홈화면 리스트 조회
    @Transactional(readOnly = true)
    public HomePatListDto getHomePatList(HomePatSearchDto homePatSearchDto) {

        Long categoryId = categoryRepository.findIdByCategoryName(homePatSearchDto.getCategory());
        HomePatListDto homePatListDto = patRepository.getHomePatList(homePatSearchDto.getLastId(), homePatSearchDto.getSize(), homePatSearchDto.getSort(),
                homePatSearchDto.getQuery(), categoryId, homePatSearchDto.isShowFull(), homePatSearchDto.getState());

        List<HomePatDto> homePatList = homePatListDto.getContent();
        for (HomePatDto homePat : homePatList) {
            homePat.setRepImg(fileService.getUrl(homePat.getRepImg()));  // 필드 값을 변경
        }
        return homePatListDto;
    }

    //맵화면 리스트 조회
    @Transactional(readOnly = true)
    public PatListDto getMapPatList(MapPatSearchDto mapPatSearchDto) {
        Long categoryId = categoryRepository.findIdByCategoryName(mapPatSearchDto.getCategory());

        PatListDto patListDto = patRepository.getMapPatList(mapPatSearchDto.getSize(), mapPatSearchDto.getQuery(), categoryId,
                mapPatSearchDto.getState(), mapPatSearchDto.isShowFull(),
                mapPatSearchDto.getLeftLongitude(), mapPatSearchDto.getRightLongitude(),
                mapPatSearchDto.getBottomLatitude(), mapPatSearchDto.getTopLatitude());

        //대표이미지 url얻어와야됨
        List<PatDto> patList = patListDto.getContent();
        for (PatDto patDto : patList) {
            patDto.setRepImg(fileService.getUrl(patDto.getRepImg()));  // 필드 값을 변경
        }
        return patListDto;
    }

    private String createDays(List<String> dayList) {
        String days = "";

        switch (dayList.size()) {
            case 7:
                days = "매일";
                break;
            case 6:
                days = "주 6회";
                break;
            case 5:
                days = "주 5회";
                break;
            case 4:
                days = "주 4회";
                break;
            default:
                // 리스트의 요일이 3개 이하인 경우, 각 요일의 첫 글자를 합친다.
                StringBuilder sb = new StringBuilder();
                for (String day : dayList) {
                    sb.append(day.charAt(0)).append(", ");
                }
                days = sb.substring(0, sb.length() - 2);
                break;
        }
        return days;
    }

    private List<PatDays> createPatDays(List<String> dayList, Pat pat) {
        List<PatDays> daysList = new ArrayList<>();
        for (String day : dayList) {
            PatDays patDays = new PatDays(pat, daysRepository.findByDayName(day));
            daysList.add(patDays);
        }
        return daysList;
    }

    private List<PatImg> createPatImg(List<List<String>> imgInfoList, Pat pat) {
        List<PatImg> patImgList = new ArrayList<>();
        //PatImg 생성
        for (List<String> image : imgInfoList
        ) {
            PatImg patImg = PatImg.builder()
                    .imgName(image.get(0))
                    .oriImgName(image.get(1))
                    .imgType(ImgType.valueOf(image.get(2)))
                    .pat(pat)
                    .build();
            patImgList.add(patImg);
        }
        return patImgList;
    }

    private int countMaxProof(List<String> days, LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;  // 두 날짜 사이의 일 수

        // 정해진 기간 동안 days에 포함된 요일 수를 계산
        int count = (int) IntStream.range(0, (int) daysBetween)
                .mapToObj(startDate::plusDays)
                .filter(date -> days.contains(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN)))
                .count();
        return count;
    }

    private void validateTwoDayDifference(LocalDate startDate, String errorMessage) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), startDate);
        if (days <= 1) {
            throw new IllegalStateException("startDate: " + errorMessage);
        }
    }

    private void validateStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days < 1) {
            throw new IllegalStateException("endDate: 종료일은 시작일 이후로 설정해주세요.");
        }
    }

    private void validateMemberPermission(Long writerId, Long memberId) {
        if (writerId != memberId) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "memberId: 사용자가 일치하지 않습니다.");
        }
    }

    private void validateMaxPersonChange(int nowPerson, int maxPerson) {
        if (maxPerson < nowPerson) {
            throw new IllegalArgumentException("maxPerson: 최대 인원은 현재 참여자와 같거나 많아야 합니다.");
        }
    }

    private void validateJoinPatDate(Long patId) {
        State state = patRepository.findStateByPatId(patId)
                .orElseThrow(() -> new IllegalArgumentException("state: 팟의 진행 현황을 확인할 수 없습니다."));
        if (state != State.SCHEDULED) {
            throw new IllegalArgumentException("state: 팟이 진행되는 도중엔 참여가 불가능합니다.");
        }
    }

    private boolean isTodayProof(Long patId, Long memberId){
        Long patMemberId = patMemberRepository.findIdByPatIdAndMemberId(patId, memberId)
                .orElseThrow(() -> new NoSuchElementException("팟 참여 정보를 찾을 수 없습니다."));
        return proofRepository.existsTodayProof(patMemberId, LocalDate.now());
    }

    private Point createPoint(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
