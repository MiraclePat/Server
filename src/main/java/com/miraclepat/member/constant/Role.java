package com.miraclepat.member.constant;

import lombok.ToString;

public enum Role {
    ROLE_USER("사용자"), ROLE_ADMIN("관리자");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
