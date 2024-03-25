package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.CartItem;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// 장바구니 조회 response
public class CartResponseDto {
    private Integer userId;
    private Store store;
    private List<CartItemResponseDto> cartItems;
    private Integer totalPrice;

    public static CartResponseDto from(List<CartItem> cartItems, User user) {
        if (cartItems.isEmpty()) {
            return new CartResponseDto(user.getUserId(), null, new ArrayList<>(), 0);
        }

        List<CartItemResponseDto> cartItemDtos = cartItems.stream()
                .map(CartItemResponseDto::new) // CartItemResponseDto 생성자가 CartItem을 받는다고 가정
                .collect(Collectors.toList());

        Integer totalPrice = cartItemDtos.stream()
                .mapToInt(dto -> dto.getPrice() * dto.getQuantity())
                .sum();

        Store store = cartItems.get(0).getStore();

        return new CartResponseDto(user.getUserId(), store, cartItemDtos, totalPrice);
    }
}
