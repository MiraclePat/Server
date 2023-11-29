package com.miraclepat.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPatListDto {

    private List<MyPatDto> content = new ArrayList<>();

    private Boolean hasNext;
}
