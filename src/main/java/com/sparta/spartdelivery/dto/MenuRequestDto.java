package com.sparta.spartdelivery.dto;

import lombok.Getter;

@Getter
public class MenuRequestDto {
    private String menuName;
    private String description;
    private Integer price;
    private String imageUrl;
}
