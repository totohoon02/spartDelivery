package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.enums.CategoryEnum;
import lombok.Getter;

@Getter
public class StoreRequestDto {
    private String storeName;
    private CategoryEnum category;
    private String phoneNumber;
    private String storeAddress;
}
