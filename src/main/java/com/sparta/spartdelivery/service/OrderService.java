package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.*;
import com.sparta.spartdelivery.entity.*;
import com.sparta.spartdelivery.enums.OrderStatusEnum;
import com.sparta.spartdelivery.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
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
    public OrderResponseDto checkout(OrderRequestDto orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<CartItem> cartItems = cartItemRepository.findByUserId(orderRequest.getUserId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderedAt(LocalDateTime.now());
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setMenu(cartItem.getMenu());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getMenu().getPrice());
            orderDetails.add(orderDetail);

            totalPrice += cartItem.getQuantity() * cartItem.getMenu().getPrice();
        }

        if (user.getPoint() < totalPrice) {
            throw new IllegalArgumentException("포인트가 충분하지 않습니다.");
        }

        newOrder.setOrderDetails(orderDetails);
        newOrder.setTotalPrice(totalPrice);
        orderRepository.save(newOrder);

        cartItemRepository.deleteAll(cartItems);

        return convertToDto(newOrder, totalPrice, orderDetails);
    }

    private OrderResponseDto convertToDto(Order order, Double totalPrice, List<OrderDetail> orderDetails) {
        OrderResponseDto responseDto = new OrderResponseDto();
        Store store = orderDetails.get(0).getMenu().getStore();
        responseDto.setPhoneNumber(store.getPhoneNumber());
        responseDto.setAddress(store.getAddress());
        responseDto.setOrderId(order.getId());
        responseDto.setTotalPrice(totalPrice);
        responseDto.setOrderStatus(order.getOrderStatus().toString());
        List<OrderDetailDto> detailDtos = orderDetails.stream().map(detail -> {
            OrderDetailDto dto = new OrderDetailDto();
            dto.setMenuId(detail.getMenu().getId());
            dto.setMenuName(detail.getMenu().getName());
            dto.setDescription(detail.getMenu().getDescription());
            dto.setQuantity(detail.getQuantity());
            dto.setPrice(detail.getPrice());
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
//    public List<OrderResponseDto> getAllOrders(Long userId) {
//        List<Order> orders = orderRepository.findByUserId(userId);
//        return orders.stream()
//                .map(this::convertToOrderResponseDto)
//                .collect(Collectors.toList());
//    }



//    public CheckoutResponseDto getOrderById(Long orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));
//        return convertToCheckoutDto(order);
//    }

//    private CheckoutResponseDto convertToCheckoutDto(Order order) {
//        CheckoutResponseDto dto = new CheckoutResponseDto();
//        dto.setOrderId(order.getId());
//        dto.setOrderStatus(order.getOrderStatus().toString());
//        dto.setOrderedAt(order.getOrderedAt());
//        dto.setDeliveredAt(order.getDeliveredAt());
//        dto.setTotalPrice(order.getTotalPrice());
//        dto.setStore(order.getStore());
////
////        List<OrderDetailDto> orderDetailDtos = order.getOrderDetails().stream()
////                .map(this::convertToOrderDetailDto)
////                .collect(Collectors.toList());
////
////        dto.setOrderDetails(orderDetailDtos);
//        return dto;
//    }
}
