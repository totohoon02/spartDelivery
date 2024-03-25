package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Integer menuId;
    private String menuName;
    private String description;
    private int quantity;
    private Integer price;

    public OrderDetailDto(OrderDetail orderDetail) {
        this.menuId = orderDetail.getMenu().getMenuId();
        this.menuName = orderDetail.getMenu().getMenuName();
        this.description = orderDetail.getMenu().getDescription();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getMenu().getPrice();
    }
}
