package com.miraclepat.utils.converter;

import com.miraclepat.pat.constant.SortType;
import com.miraclepat.pat.constant.State;
import org.springframework.core.convert.converter.Converter;

public class StringToSortTypeConverter implements Converter<String, SortType> {
    @Override
    public SortType convert(String source) {
        return SortType.valueOf(source.toUpperCase());
    }
}