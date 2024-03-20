package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Order;
import com.sparta.spartdelivery.entity.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;

public class BossOrderResponseDto {
    private Integer orderId;
    private String phoneNumber;
    private String address;
    private String orderStatus;
    private Integer totalPrice;
    private LocalDateTime orderedAt;
    private List<BossOrderDetailDto> orderDetails;

    public BossOrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.phoneNumber = order.getUser().getPhoneNumber();
        this.address = order.getUser().getAddress();
        this.orderStatus = order.getOrderStatusEnum().getValue();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();
        this.orderDetails = order.getOrderDetails().stream().map(BossOrderDetailDto::new).toList();
    }
    private class BossOrderDetailDto{
        private Integer orderDetailId;
        private String imageUrl;
        private String menuName;
        private Integer price;
        private Short quantity;

        // TODO: orderDetail price 가 menu테이블을 인용할 경우, menu update 시에 주문 가격 변동 됨 -> 사장님매출 변동
        public BossOrderDetailDto(OrderDetail orderDetail) {
            this.orderDetailId = orderDetail.getOrderDetailId();
            this.imageUrl = orderDetail.getMenu().getImageUrl();
            this.menuName = orderDetail.getMenu().getMenuName();
            this.price = orderDetail.getMenu().getPrice();
            this.quantity = orderDetail.getQuantity();
        }
    }
}
