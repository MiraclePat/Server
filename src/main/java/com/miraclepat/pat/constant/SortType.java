package com.miraclepat.pat.constant;

public enum SortType {

    HOT("인기순"),
    LATEST("최신순");

    private String description;

    SortType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
