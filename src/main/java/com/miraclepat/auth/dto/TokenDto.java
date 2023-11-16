package com.miraclepat.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TokenDto {
    String token;

    public TokenDto(String token) {
        this.token = token;
    }
}
