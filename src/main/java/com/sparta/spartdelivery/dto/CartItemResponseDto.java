package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDto {
    private Long menuId;
    private String name;
    private int quantity;
    private double price;
    private String description;
}
