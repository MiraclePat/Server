package com.miraclepat.pat.dto;

import com.miraclepat.pat.constant.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapPatSearchDto {

    private int size = 10;
    private String query;
    private String category;
    private State state;
    private boolean showFull = false;

    @Min(-180)
    @Max(180)
    private Double leftLongitude = 10.0;

    @Min(-180)
    @Max(180)
    private Double rightLongitude = 80.0;

    @Min(-90)
    @Max(90)
    private Double bottomLatitude = 10.0;

    @Min(-90)
    @Max(90)
    private Double topLatitude = 80.0;
}
