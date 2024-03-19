package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.util.EnumModel;

public enum CategoryEnum implements EnumModel {

    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    FASTFOOD("패스트푸드");

    private String value;

    CategoryEnum(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
    // value 값으로 category 찾기
    // "양식" -> WESTERN("양식")
    public static CategoryEnum findByValue(String value) {
        for (CategoryEnum category : CategoryEnum.values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        return null;
    }
}
