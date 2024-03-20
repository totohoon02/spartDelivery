package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class GetOrderListResponseDto {
        private Integer orderId;
        private String address;
        private Integer totalPrice;
        private LocalDateTime orderedAt;
        private String orderStatus;

        public GetOrderListResponseDto(Order order) {
                this.orderId = order.getOrderId();
                this.address = order.getUser().getAddress();
                this.totalPrice = order.getTotalPrice();
                this.orderedAt = order.getOrderedAt();
                this.orderStatus = order.getOrderStatusEnum().getValue();
        }
}