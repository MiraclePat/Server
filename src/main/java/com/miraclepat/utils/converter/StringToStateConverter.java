package com.miraclepat.utils.converter;

import com.miraclepat.pat.constant.State;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStateConverter implements Converter<String, State> {
    @Override
    public State convert(String source) {
        return State.valueOf(source.toUpperCase());
    }
}
