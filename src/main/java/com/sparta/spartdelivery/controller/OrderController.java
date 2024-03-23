package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.BossOrderResponseDto;
import com.sparta.spartdelivery.dto.GetOrderListResponseDto;
import com.sparta.spartdelivery.dto.OrderResponseDto;
import com.sparta.spartdelivery.dto.PutOrderResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.enums.UserRoleEnum;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Secured(UserRoleEnum.Authority.BOSS)
    @GetMapping("")
    public String getOrders(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<GetOrderListResponseDto> orderInfos = orderService.getOrderList(user);
        model.addAttribute("orderInfos", orderInfos);
        return "order_manage";
    }

    // 결제하고 주문 생성
    @PostMapping()
    public ResponseEntity<OrderResponseDto> checkout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        OrderResponseDto orderResponse = orderService.checkout(user);
        return ResponseEntity.ok(orderResponse);
    }

    // 고객 주문 확인
    @GetMapping("/customer/{orderId}")
    public String getCustomerOrderDetails(@PathVariable Integer orderId, Model model) {
        OrderResponseDto orderResponseDto = orderService.getCustomerOrderDetails(orderId);
        model.addAttribute("orders", orderResponseDto);
        return "checkout";
    }

    @GetMapping("/boss/{orderId}")
    public String getBossOrderDetails(@PathVariable Integer orderId, Model model) {
        BossOrderResponseDto bossOrderResponseDto = orderService.getBossOrderDetails(orderId);
        model.addAttribute("orderInfos", bossOrderResponseDto);
        return "order_detail";
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<PutOrderResponseDto> markOrderAsDelivered(@PathVariable Integer orderId) {

        PutOrderResponseDto putOrderResponseDto = orderService.markOrderAsDelivered(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(putOrderResponseDto);
    }
}

