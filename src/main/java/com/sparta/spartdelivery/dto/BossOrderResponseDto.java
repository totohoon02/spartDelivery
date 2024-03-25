package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Order;
import com.sparta.spartdelivery.entity.OrderDetail;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
public class BossOrderResponseDto {
    private final Integer orderId;
    private final String phoneNumber;
    private final String address;
    private final String orderStatus;
    private final Integer totalPrice;
    private final LocalDateTime orderedAt;
    private final List<BossOrderDetailDto> orderDetails;

    public BossOrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.phoneNumber = order.getUser().getPhoneNumber();
        this.address = order.getUser().getAddress();
        this.orderStatus = order.getOrderStatusEnum().getValue();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();
        this.orderDetails = order.getOrderDetails().stream().map(BossOrderDetailDto::new).toList();
    }
    @Getter
    public class BossOrderDetailDto{
        private final Integer orderDetailId;
        private final String imageUrl;
        private final String menuName;
        private final Integer price;
        private final Short quantity;

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
