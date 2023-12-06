package com.miraclepat.pat.controller;

import com.miraclepat.pat.dto.*;
import com.miraclepat.pat.service.PatImgService;
import com.miraclepat.pat.service.PatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/pats")
@RequiredArgsConstructor
public class PatController {

    private final PatService patService;
    private final PatImgService patImgService;

    @GetMapping("/home/banner")
    //홈 화면
    public ResponseEntity<HomeBannerDto> getHomeBanner(Principal principal) {
        HomeBannerDto homeBannerDto = patService.getHomeBanner(1L);
        return ResponseEntity.ok().body(homeBannerDto);
    }

    @GetMapping("/home")
    //홈 화면
    public ResponseEntity<HomePatListDto> getPatList(
            @ModelAttribute HomePatSearchDto homePatSearchDto
    ) {
        HomePatListDto homePatListDto = patService.getHomePatList(homePatSearchDto);
        return ResponseEntity.ok().body(homePatListDto);
    }

    @GetMapping("/map")
    //맵 화면
    public ResponseEntity<PatListDto> getPatList(
            @ModelAttribute MapPatSearchDto mapPatSearchDto
    ) {
        PatListDto PatListDto = patService.getMapPatList(mapPatSearchDto);
        return ResponseEntity.ok(PatListDto);
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
        List<List<String>> imgInfoList = patImgService.uploadPatImg(repImg, correctImg, incorrectImg, bodyImg);
        patService.createPat(createPatDto, imgInfoList, 1L);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //상세보기
    @GetMapping("/{pat-id}")
    public ResponseEntity getPatDetail(@PathVariable("pat-id") Long patId, Principal principal) {
        PatDetailDto detailDto = patService.detailPat(patId, 1L);
        return ResponseEntity.ok().body(detailDto);
    }

    //참여하기
    @PostMapping("/{pat-id}")
    public ResponseEntity joinPat(@PathVariable("pat-id") Long patId, Principal principal) {
        patService.joinPat(patId, 2L);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //수정하기
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
        List<List<String>> imgInfoList = patImgService.uploadPatImg(repImg, correctImg, incorrectImg, bodyImg);
        patService.updatePat(createPatDto, imgInfoList, 1L, patId);
        return ResponseEntity.noContent().build();
    }

    //팟 삭제 -> leader만 시작 전에
    @DeleteMapping("/{pat-id}")
    public ResponseEntity deletePat(@PathVariable("pat-id") Long patId, Principal principal) {
        patService.deletePat(patId, 1L);
        return ResponseEntity.noContent().build();
    }

    //팟 가입 신청 취소
    @DeleteMapping("/{pat-id}/withdraw")
    public ResponseEntity withdrawPat(@PathVariable("pat-id") Long patId, Principal principal) {
        patService.withdrawPat(patId, 2L);
        return ResponseEntity.noContent().build();
    }

}
