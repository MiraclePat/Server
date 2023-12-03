package com.miraclepat.pat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Getter
@NoArgsConstructor
public class HomeBannerDto {
    //팟id, 팟 제목, date - 시작일부터 며칠 지났는지.

    Long id;

    String patName;

    String date;

    public HomeBannerDto(Long id, String patName, LocalDate startDate) {
        this.id = id;
        this.patName = patName;
        this.date = "D+"+calcDays(startDate);
    }

    private long calcDays(LocalDate startDate){
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(startDate, today);
        return daysBetween;
    }
}
