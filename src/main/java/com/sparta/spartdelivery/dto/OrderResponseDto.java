package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.OrderDetail;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.enums.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;
    private String phoneNumber;
    private String address;
    private Double totalPrice;
    private String orderStatus;
    private List<OrderDetailDto> orderDetails;
}
