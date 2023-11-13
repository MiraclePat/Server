package com.miraclepat.pat.controller;

import com.miraclepat.category.entity.Category;
import com.miraclepat.pat.dto.CreatePatDto;
import com.miraclepat.pat.dto.PatDetailDto;
import com.miraclepat.pat.dto.PatListDto;
import com.miraclepat.pat.entity.Pat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

;

@Validated
@RestController
@RequestMapping("/api/test/pats")
public class TestPatController {

    @GetMapping("/map")
    //맵 화면
    public ResponseEntity<PatListDto> getPatList(
            @RequestParam(name = "page", required = false, defaultValue = "1")
            Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            Integer size,
            @RequestParam(name = "search", required = false)
            String search,
            @RequestParam(name = "category", required = false)
            String category,
            @RequestParam(name = "leftLongitude", defaultValue = "0.0")
            Double leftLongitude,
            @RequestParam(name = "rightLongitude", defaultValue = "0.0")
            Double rightLongitude,
            @RequestParam(name = "bottomLatitude", defaultValue = "0.0")
            Double bottomLatitude,
            @RequestParam(name = "topLatitude", defaultValue = "0.0")
            Double topLatitude
    ) {
        Category category1 = new Category();
        category1.setCategoryName("환경");

        List<Pat> list = new ArrayList<>();

        for(int i = 0;i<5;i++){
            Pat pat = new Pat();
            pat.setId(Long.valueOf(i));
            pat.setPatDetail("디테일"+i);
            pat.setPatName("제목 들어가는 곳.조회"+i);
            pat.setDays("월,화,수");
            pat.setCategory(category1);
            pat.setLeader("윈터");
            pat.setEndDate(LocalDate.now());
            pat.setStartDate(LocalDate.now());
            pat.setStartTime(new Time(System.currentTimeMillis()).toLocalTime());
            pat.setEndTime(new Time(System.currentTimeMillis()).toLocalTime());
            pat.setPosition(pat.createPoint(37.482778-(0.002*i),126.927592-(0.002*i)));
            pat.setLocation("서울특별시 관악구 신림동");
            pat.setNowPerson(8);
            pat.setMaxPerson(10);
            pat.setRealtime("Y");
            pat.setProofDetail("쓰레기 줍고 다니기");
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
            MultipartFile repImg ,
            @Size(min = 1, max = 1, message = "옳은 예제 이미지는 1개까지 업로드할 수 있습니다.")
            @RequestPart("correctImg")
            List<MultipartFile> correctImg,
            @Size(max = 3, message = "틀린 예제 이미지는 최대 3개까지 업로드할 수 있습니다.")
            @RequestPart(value = "incorrectImg", required = false)
            List<MultipartFile> incorrectImg,
            @Size(max = 5, message = "본문 이미지는 최대 5개까지 업로드할 수 있습니다.")
            @RequestPart(value = "bodyImg", required = false)
            List<MultipartFile> bodyImg
    ){

        return ResponseEntity.noContent().build();
    }

    //상세보기
    @GetMapping("/{pat-id}")
    public ResponseEntity getPatDetail(@PathVariable("pat-id") int id){

        Category category1 = new Category();
        category1.setCategoryName("환경");

        Pat pat = new Pat();
        pat.setId(Long.valueOf(id));
        pat.setPatDetail("상세조회 본문");
        pat.setPatName("상세조회 성공!!!!");
        pat.setDays("월,화,수,목,금");
        pat.setCategory(category1);
        pat.setLeader("윈터");
        pat.setEndDate(LocalDate.now().plusDays(2));
        pat.setStartDate(LocalDate.now());
        pat.setStartTime(new Time(System.currentTimeMillis()).toLocalTime());
        pat.setEndTime(new Time(System.currentTimeMillis()).toLocalTime());
        pat.setPosition(pat.createPoint(37.482778,126.927592));
        pat.setLocation("서울특별시 관악구 신림동");
        pat.setNowPerson(8);
        pat.setMaxPerson(10);
        pat.setRealtime("Y");
        pat.setMaxProof(10);
        pat.setProofDetail("쓰레기 줍고 다니기");

        PatDetailDto detailDto = new PatDetailDto(pat);

        return ResponseEntity.ok().body(detailDto);
    }

    //참여하기
    @PostMapping("/{pat-id}")
    public ResponseEntity joinPat(){
        return ResponseEntity.noContent().build();
    }

    //수정하기 -> leader만 가능~
    @PatchMapping("/{pat-id}")
    public ResponseEntity updatePat(){
        return ResponseEntity.noContent().build();
    }

    //팟 삭제 -> leader만 시작 전에
    @DeleteMapping("/{pat-id}")
    public ResponseEntity deletePat(){
        return ResponseEntity.noContent().build();
    }

    //팟 가입 신청 취소
    @DeleteMapping("/{pat-id}/withdraw")
    public ResponseEntity withdrawPat(){
        return ResponseEntity.noContent().build();
    }

}
