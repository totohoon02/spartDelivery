package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDto {
    private Integer menuId;
    private String menuName;
    private Short quantity;
    private Integer price;
    private String description;
}
