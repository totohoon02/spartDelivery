package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.OrderResponseDto;
import com.sparta.spartdelivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 결제하고 주문 생성
    @PostMapping()
    public ResponseEntity<OrderResponseDto> checkout() {
        OrderResponseDto orderResponse = orderService.checkout();
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, Model model) {
        OrderResponseDto orderResponseDto = orderService.getOrderDetails(orderId);
        model.addAttribute("orders", orderResponseDto);
        return "checkout";
    }

//    @GetMapping("/{orderId}")
//    public ResponseEntity<CheckoutResponseDto> getOrderById(@PathVariable Long orderId) {
//        CheckoutResponseDto checkoutResponse = orderService.getOrderById(orderId); // Assuming such a method exists
//       return ResponseEntity.ok(checkoutResponse);
//    }

//    // 모든 주문 보기 (사용자용) -> userId는 임시, auth로 받아올것
//    @GetMapping("/{userId}")
//    public String getAllOrdersByUser(@PathVariable Long userId, Model model) {
//        List<OrderResponseDto> orders = orderService.getAllOrders(userId);
//        model.addAttribute("orders", orders);
//        return "checkout";
//    }
}

