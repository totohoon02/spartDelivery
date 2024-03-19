package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 장바구니 추가 request body
public class CartAddItemRequestDto {
    private Long userId;

    private Long menuId;

    private Long storeId;

}
