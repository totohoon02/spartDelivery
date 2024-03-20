package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
// 장바구니 조회 response
public class CartResponseDto {
    private Integer userId;
    private Store store;
    private List<CartItemResponseDto> cartItems;
    private Integer totalPrice;
}
