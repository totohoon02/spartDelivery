package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class CheckoutResponseDto {
    private Long orderId;
    private Store store;
    private String orderStatus;
    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
    private List<OrderDetailDto> orderDetails;
    private Integer totalPrice;
}
