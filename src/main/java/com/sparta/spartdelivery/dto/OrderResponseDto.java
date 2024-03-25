package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Integer orderId;
    private String phoneNumber;
    private String address;
    private Integer totalPrice;
    private String orderStatus;
    private List<OrderDetailDto> orderDetails;

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.phoneNumber = order.getStore().getPhoneNumber();
        this.address = order.getStore().getAddress();
        this.totalPrice = order.getTotalPrice();
        this.orderStatus = order.getOrderStatusEnum().getValue();
        this.orderDetails = order.getOrderDetails().stream().map(OrderDetailDto::new).toList();
    }
}
