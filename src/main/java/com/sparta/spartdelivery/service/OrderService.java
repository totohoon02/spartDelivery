package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.*;
import com.sparta.spartdelivery.entity.CartItem;
import com.sparta.spartdelivery.entity.Order;
import com.sparta.spartdelivery.entity.OrderDetail;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.enums.OrderStatusEnum;
import com.sparta.spartdelivery.repository.CartItemRepository;
import com.sparta.spartdelivery.repository.OrderRepository;
import com.sparta.spartdelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
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
        // 1. user id 로 새로운 User 객체 생성
        User client = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        // 2. CartItems 가져 오기
        List<CartItem> cartItems = getValidCartItems(client);

        // 3. 카트 결제 금액 계산
        Integer totalPrice = calculateTotalPrice(cartItems);

        // 4. 카트 Item 으로 Order, Orderdetail
        Order newOrder = createOrder(client, cartItems, totalPrice);

        // 5. 유저 포인트 충분 여부 검증 및 포인트 차감
        client.withdrawPoint(totalPrice);

        // 6. 배달 주문 시, 사장 포인트 적립
        User boss = userRepository.findByStoreId(newOrder.getStore().getStoreId());
        boss.depositPoint(totalPrice);

        // 카트 삭제
        cartItemRepository.deleteAll(cartItems);

        return new OrderResponseDto(newOrder);
    }

    public OrderResponseDto getCustomerOrderDetails(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        return new OrderResponseDto(order);
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

    public List<CartItem> getValidCartItems(User user) {
        // 1. 해당 User가 CartItem 을 가지고 있는지 확인
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        // 2. CartItems 비었는지 확인
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }
        return cartItems;
    }

    private Integer calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(cartItem -> cartItem.getQuantity() * cartItem.getMenu().getPrice())
                .sum();
    }

    public Order createOrder(User user, List<CartItem> cartItems, Integer totalPrice) {
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setOrderedAt(LocalDateTime.now());
        newOrder.setStore(cartItems.get(0).getStore());
        newOrder.setOrderStatusEnum(OrderStatusEnum.ORDERED);

        List<OrderDetail> orderDetails = cartItems.stream()
                .map(cartItem -> convertToOrderDetail(cartItem, newOrder))
                .collect(Collectors.toList());

        newOrder.setOrderDetails(orderDetails);
        orderRepository.save(newOrder);

        return newOrder;
    }

    private OrderDetail convertToOrderDetail(CartItem cartItem, Order order) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setMenu(cartItem.getMenu());
        orderDetail.setQuantity(cartItem.getQuantity());
        return orderDetail;
    }

}
