package com.miraclepat.pat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class PatTimeDto {
    LocalTime starTime;
    LocalTime endTime;

    public PatTimeDto(LocalTime starTime, LocalTime endTime) {
        this.starTime = starTime;
        this.endTime = endTime;
    }
}
