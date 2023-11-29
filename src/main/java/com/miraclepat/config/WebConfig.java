package com.miraclepat.config;

import com.miraclepat.utils.converter.StringToSortTypeConverter;
import com.miraclepat.utils.converter.StringToStateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToStateConverter());
        registry.addConverter(new StringToSortTypeConverter());
    }
}
