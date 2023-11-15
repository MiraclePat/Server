package com.miraclepat.pat.constant;

public enum State {

    SCHEDULED("진행 예정"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료");

    private String description;

    State(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }


    }
