package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreDetailResponseDto {
    private Integer storeId;
    private String storeName;
    private String phoneNumber;
    private String address;
    private String description;
    private String category;
    private Double rating;
    private List<MenuResponseDto> menus;
    private List<ReviewResponseDto> reviews;
}
