package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class PutOrderResponseDto {
    private Integer orderId;
    private String address;
    private Integer totalPrice;
    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
    private String orderStatus;

    public PutOrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.address = order.getUser().getAddress();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();
        this.deliveredAt = order.getDeliveredAt();
        this.orderStatus = order.getOrderStatusEnum().getValue();
    }
}