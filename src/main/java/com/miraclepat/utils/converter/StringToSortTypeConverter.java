package com.miraclepat.utils.converter;

import com.miraclepat.pat.constant.State;
import org.springframework.core.convert.converter.Converter;

public class StringToSortTypeConverter implements Converter<String, State> {
    @Override
    public State convert(String source) {
        return State.valueOf(source.toUpperCase());
    }
}