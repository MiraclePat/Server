package com.miraclepat.pat.controller;

import com.miraclepat.category.entity.Category;
import com.miraclepat.pat.constant.State;
import com.miraclepat.pat.dto.*;
import com.miraclepat.pat.entity.Pat;
import com.miraclepat.utils.Constants;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

;

@Validated
@RestController
@RequestMapping("/api/test/pats")
public class TestPatController {
    //홈화면 리스트, 맵화면 리스트, 팟 상세보기, 팟 생성, 수정, 삭제, 참여, 삭제, 팟 탈퇴

    @GetMapping("/home/banner")
    //홈 화면
    public ResponseEntity<HomeBannerDto> getHomeBanner(Principal principal) {
        HomeBannerDto homeBannerDto = new HomeBannerDto(1L, "홈 배너 test", LocalDate.now().minusDays(5));
        return ResponseEntity.ok().body(homeBannerDto);
    }

    @GetMapping("/home")
    //홈 화면
    public ResponseEntity<HomePatListDto> getPatList(
            @ModelAttribute HomePatSearchDto homePatSearchDto
    ) {
        List<Category> c = new ArrayList<>();

        Category category0 = new Category(1L, "환경");
        Category category1 = new Category(2L, "취미");
        Category category2 = new Category(3L, "기타");
        Category category3 = new Category(4L, "일상");
        Category category4 = new Category(5L, "건강");

        c.add(category0);
        c.add(category1);
        c.add(category2);
        c.add(category3);
        c.add(category4);

        List<HomePatDto> list = new ArrayList<>();

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
                    .proofDetail("길거리에서 쓰레기 줍고 다니기")
                    .build();
            list.add(new HomePatDto(pat));
        }
        HomePatListDto homePatListDto = new HomePatListDto(list, true);
        return ResponseEntity.ok().body(homePatListDto);
    }

    @GetMapping("/map")
    //맵 화면
    public ResponseEntity<PatListDto> getPatList(
            @RequestParam(name = "size", required = false, defaultValue = "10")
            int size,
            @RequestParam(name = "query", required = false)
            String query,
            @RequestParam(name = "category", required = false)
            String category,
            @RequestParam(name = "state", required = false)
            State state,
            @RequestParam(name = "showFull", required = false, defaultValue = "false")
            boolean showFull,
            @RequestParam(name = "leftLongitude", defaultValue = "10.0")
            Double leftLongitude,
            @RequestParam(name = "rightLongitude", defaultValue = "80.0")
            Double rightLongitude,
            @RequestParam(name = "bottomLatitude", defaultValue = "10.0")
            Double bottomLatitude,
            @RequestParam(name = "topLatitude", defaultValue = "80.0")
            Double topLatitude
    ) {

        List<Category> c = new ArrayList<>();

        Category category0 = new Category(1L, "환경");
        Category category1 = new Category(2L, "취미");
        Category category2 = new Category(3L, "기타");
        Category category3 = new Category(4L, "일상");
        Category category4 = new Category(5L, "건강");

        c.add(category0);
        c.add(category1);
        c.add(category2);
        c.add(category3);
        c.add(category4);

        List<PatDto> list = new ArrayList<>();

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(127.2, 37.2));

        for (int i = 0; i < 5; i++) {
            PatDto pat = new PatDto(Long.valueOf(i), Constants.REP_IMG, "맵화면 예시" + i,
                    LocalDate.now().plusDays(i), c.get(i).getCategoryName(), point, 2, 10, "월, 수", "서울시");
            list.add(pat);
        }

        PatListDto patListResponse = new PatListDto(list);

        return ResponseEntity.ok().body(patListResponse);
    }

    //팟 생성
    @PostMapping
    public ResponseEntity createPat(
            @Valid
            @RequestPart("pat")
            CreatePatDto createPatDto,
            @RequestPart("repImg")
            MultipartFile repImg,
            @RequestPart("correctImg")
            MultipartFile correctImg,
            @RequestPart(value = "incorrectImg", required = false)
            MultipartFile incorrectImg,
            @Size(max = 5, message = "본문 이미지는 최대 5개까지 업로드할 수 있습니다.")
            @RequestPart(value = "bodyImg", required = false)
            List<MultipartFile> bodyImg
    ) {


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //상세보기
    @GetMapping("/{pat-id}")
    public ResponseEntity getPatDetail(@PathVariable("pat-id") Long patId) {

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
                .build();

        PatDetailDto detailDto = new PatDetailDto(pat);

        return ResponseEntity.ok().body(detailDto);
    }

    //참여하기
    @PostMapping("/{pat-id}")
    public ResponseEntity joinPat(@PathVariable("pat-id") Long patId) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //수정하기 -> leader만 가능~
    @PatchMapping("/{pat-id}")
    public ResponseEntity updatePat(
            @Valid
            @RequestPart("pat")
            CreatePatDto createPatDto,
            @RequestPart("repImg")
            MultipartFile repImg,
            @RequestPart("correctImg")
            MultipartFile correctImg,
            @RequestPart(value = "incorrectImg", required = false)
            MultipartFile incorrectImg,
            @Size(max = 5, message = "본문 이미지는 최대 5개까지 업로드할 수 있습니다.")
            @RequestPart(value = "bodyImg", required = false)
            List<MultipartFile> bodyImg,
            @PathVariable("pat-id") Long patId
    ) {
        return ResponseEntity.noContent().build();
    }

    //팟 삭제 -> leader만 시작 전에
    @DeleteMapping("/{pat-id}")
    public ResponseEntity deletePat(@PathVariable("pat-id") Long patId) {
        return ResponseEntity.noContent().build();
    }

    //팟 가입 신청 취소
    @DeleteMapping("/{pat-id}/withdraw")
    public ResponseEntity withdrawPat(@PathVariable("pat-id") Long patId) {
        return ResponseEntity.noContent().build();
    }

}
