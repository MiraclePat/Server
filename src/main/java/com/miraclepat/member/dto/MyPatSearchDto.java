package com.miraclepat.member.dto;

import com.miraclepat.pat.constant.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPatSearchDto {
    private Long lastId;
    private int size = 10; // 기본값 10
    private State state;
}
