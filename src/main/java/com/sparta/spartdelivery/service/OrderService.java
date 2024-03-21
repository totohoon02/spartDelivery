package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.*;
import com.sparta.spartdelivery.entity.*;
import com.sparta.spartdelivery.enums.OrderStatusEnum;
import com.sparta.spartdelivery.repository.CartItemRepository;
import com.sparta.spartdelivery.repository.OrderRepository;
import com.sparta.spartdelivery.repository.UserRepository;
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

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }
    // 주문 리스트 조회하기
    public List<GetOrderListResponseDto> getOrderList(User user) {
        Integer storeId = user.getStoreId();
        List<Order> orderList = orderRepository.findByStore_storeId(storeId);
        return orderList.stream().map(GetOrderListResponseDto::new)
                .toList();
    }


    // 결제하기
    @Transactional
    public OrderResponseDto checkout(User user) {
        User client = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }

        Order newOrder = new Order();

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

        if (client.getPoint() < totalPrice) {
            throw new IllegalArgumentException("포인트가 충분하지 않습니다.");
        }

        // 유저 포인트 차감
        client.setPoint(user.getPoint() - totalPrice);

        // 주문 정보 저장
        newOrder.setUser(client);

        newOrder.setOrderDetails(orderDetails);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setOrderStatusEnum(OrderStatusEnum.ORDERED);
        newOrder.setOrderedAt(LocalDateTime.now());
        Store store = cartItems.get(0).getStore();
        newOrder.setStore(store);

        orderRepository.save(newOrder);

        // 배달 주문 시, 사장 포인트 적립
        User boss = userRepository.findByStoreId(store.getStoreId());
        boss.setPoint(boss.getPoint() + totalPrice);

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
        responseDto.setOrderStatus(order.getOrderStatusEnum().getValue());

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

    public OrderResponseDto getCustomerOrderDetails(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        return convertToDto(order, order.getTotalPrice(), order.getOrderDetails());
    }

    public BossOrderResponseDto getBossOrderDetails(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        return new BossOrderResponseDto(order);
    }

    @Transactional
    public PutOrderResponseDto markOrderAsDelivered(Integer orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        if (order.getOrderStatusEnum().equals(OrderStatusEnum.ORDERED)) {
            order.markOrderAsDelivered(OrderStatusEnum.DELIVERED, LocalDateTime.now());
        } else {
            throw new IllegalArgumentException("이미 배달완료된 주문 입니다.");
        }

        return new PutOrderResponseDto(order);
    }
}
