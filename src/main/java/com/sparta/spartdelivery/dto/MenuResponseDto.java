package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuResponseDto {
    private Long menuId;
    private String name;
    private double price;
    private String imageUrl;
    private String description;


}