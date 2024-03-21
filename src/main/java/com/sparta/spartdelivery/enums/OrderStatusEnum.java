package com.sparta.spartdelivery.enums;


import com.sparta.spartdelivery.util.EnumModel;

public enum OrderStatusEnum implements EnumModel {

    ORDERED("주문완료"),
    DELIVERED("배달완료");

    private String value;

    OrderStatusEnum(String value) {
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
}
