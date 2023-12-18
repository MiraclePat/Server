package com.miraclepat.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenDto {
    String token;

    public TokenDto(String token) {
        this.token = token;
    }
}
