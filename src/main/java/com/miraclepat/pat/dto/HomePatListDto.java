package com.miraclepat.pat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomePatListDto {
    private List<HomePatDto> content = new ArrayList<>();
    private Boolean hasNext;
}
