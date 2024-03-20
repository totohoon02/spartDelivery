package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.CartItem;
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

    public CartItemResponseDto(CartItem cartItem) {
        this.menuId = cartItem.getMenu().getMenuId();
        this.menuName = cartItem.getMenu().getMenuName();
        this.price = cartItem.getMenu().getPrice();
        this.description = cartItem.getMenu().getDescription();
        this.quantity = cartItem.getQuantity();
    }

    public CartItemResponseDto() {
    }
}
