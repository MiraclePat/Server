package com.miraclepat.pat.dto;

import com.miraclepat.pat.constant.SortType;
import com.miraclepat.pat.constant.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomePatSearchDto {
    private Long lastId;
    private int size = 10; // 기본값 10
    private SortType sort = SortType.LATEST; // 기본값 "LATEST"
    private String query;
    private String category;
    private boolean showFull = false; // 기본값 false
    private State state;
}
