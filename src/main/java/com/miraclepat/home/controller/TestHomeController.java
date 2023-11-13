package com.miraclepat.home.controller;

import com.miraclepat.category.entity.Category;
import com.miraclepat.home.dto.HomePatListDto;
import com.miraclepat.pat.entity.Pat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/test/pats/home")
public class TestHomeController {
    @GetMapping
    //홈 화면
    public ResponseEntity<HomePatListDto> getPatList(
            @RequestParam(name = "page", required = false, defaultValue = "0")
            Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            Integer size,
            @RequestParam(name = "sort", defaultValue = "createdTime")
            String sort,
            @RequestParam(name = "search", required = false)
            String search,
            @RequestParam(name = "category", required = false)
            String category
    ) {
        Category category1 = new Category();
        category1.setCategoryName("환경");

        List<Pat> list = new ArrayList<>();

        for(int i = 0;i<5;i++){
            Pat pat = new Pat();
            pat.setId(Long.valueOf(i));
            pat.setPatDetail("디테일"+i);
            pat.setPatName("제목입니다.홈화면"+i);
            pat.setDays("월,화,수");
            pat.setCategory(category1);
            pat.setLeader("윈터");
            pat.setEndDate(LocalDate.now().plusDays(i*2));
            pat.setStartDate(LocalDate.now().plusDays(i));
            pat.setStartTime(new Time(System.currentTimeMillis()).toLocalTime());
            pat.setEndTime(new Time(System.currentTimeMillis()).toLocalTime().plusHours(i));
            pat.setPosition(pat.createPoint(37.482778-(0.002*i),126.927592-(0.002*i)));
            pat.setLocation("서울특별시 관악구 신림동");
            pat.setNowPerson(8);
            pat.setMaxPerson(10);
            pat.setRealtime("Y");
            pat.setProofDetail("쓰레기 줍고 다니기");
            list.add(pat);
        }

        HomePatListDto homePatListDto = new HomePatListDto(list);

        return ResponseEntity.ok().body(homePatListDto);
    }
}
