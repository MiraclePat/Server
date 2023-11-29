package com.miraclepat.pat.constant;

public enum ButtonState {
    CANCELABLE("변경 가능"),
    NO_CANCELABLE("변경 불가"),
    IN_PROGRESS("인증 기간"),
    COMPLETED("인증 기간 종료");

    private String description;

    ButtonState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
