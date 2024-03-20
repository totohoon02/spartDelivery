package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.OrderDetailDto;
import com.sparta.spartdelivery.dto.OrderResponseDto;
import com.sparta.spartdelivery.entity.*;
import com.sparta.spartdelivery.enums.OrderStatusEnum;
import com.sparta.spartdelivery.repository.CartItemRepository;
import com.sparta.spartdelivery.repository.OrderRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import com.sparta.spartdelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CartItemRepository cartItemRepository;
    private StoreRepository storeRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        CartItemRepository cartItemRepository, StoreRepository storeRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.storeRepository = storeRepository;
    }


    // 결제하기
    @Transactional
    public OrderResponseDto checkout() {
        // Obtain the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Ensure there is an authenticated user
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user");
        }

        // Assuming the principal can be cast to UserDetails and contains the username (email)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername(); // Here, username is the user's email.

        // Find the user by email (or username)
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }

        Order newOrder = new Order();
        newOrder.setUserId(user.getUserId());
        newOrder.setOrderedAt(LocalDateTime.now());
        List<OrderDetail> orderDetails = new ArrayList<>();
        Integer totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setMenu(cartItem.getMenu());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetails.add(orderDetail);

            totalPrice += cartItem.getQuantity() * cartItem.getMenu().getPrice();
        }

        if (user.getPoint() < totalPrice) {
            throw new IllegalArgumentException("포인트가 충분하지 않습니다.");
        }

        newOrder.setOrderDetails(orderDetails);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setOrderStatusEnum(OrderStatusEnum.ORDERED);
        orderRepository.save(newOrder);

        cartItemRepository.deleteAll(cartItems);

        return convertToDto(newOrder, totalPrice, orderDetails);
    }

    private OrderResponseDto convertToDto(Order order, Integer totalPrice, List<OrderDetail> orderDetails) {
        OrderResponseDto responseDto = new OrderResponseDto();
        Store store = orderDetails.get(0).getMenu().getStore();

        responseDto.setPhoneNumber(store.getPhoneNumber());
        responseDto.setAddress(store.getAddress());
        responseDto.setOrderId(order.getOrderId());
        responseDto.setTotalPrice(totalPrice);
        responseDto.setOrderStatus(order.getOrderStatusEnum().toString());

        List<OrderDetailDto> detailDtos = orderDetails.stream().map(detail -> {
            OrderDetailDto dto = new OrderDetailDto();
            dto.setMenuId(detail.getMenu().getMenuId());
            dto.setMenuName(detail.getMenu().getMenuName());
            dto.setDescription(detail.getMenu().getDescription());
            dto.setQuantity(detail.getQuantity());
            dto.setPrice(detail.getMenu().getPrice());
            return dto;
        }).collect(Collectors.toList());
        responseDto.setOrderDetails(detailDtos);

        return responseDto;
    }

    public OrderResponseDto getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        return convertToDto(order, order.getTotalPrice(), order.getOrderDetails());
    }
}
