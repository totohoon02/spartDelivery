package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long menuId;
    private Integer quantity;
}
