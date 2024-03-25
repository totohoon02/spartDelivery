package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class GetOrderListResponseDto {
        private final Integer orderId;
        private final String address;
        private final Integer totalPrice;
        private final LocalDateTime orderedAt;
        private final String orderStatus;

        public GetOrderListResponseDto(Order order) {
                this.orderId = order.getOrderId();
                this.address = order.getUser().getAddress();
                this.totalPrice = order.getTotalPrice();
                this.orderedAt = order.getOrderedAt();
                this.orderStatus = order.getOrderStatusEnum().getValue();
        }
}