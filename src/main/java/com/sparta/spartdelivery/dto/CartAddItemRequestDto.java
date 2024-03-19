package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 장바구니 추가 request body
public class CartAddItemRequestDto {
    private Integer userId;

    private Integer menuId;

    private Integer storeId;

}
