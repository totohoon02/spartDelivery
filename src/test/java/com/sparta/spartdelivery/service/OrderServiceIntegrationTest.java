package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.OrderResponseDto;
import com.sparta.spartdelivery.entity.*;
import com.sparta.spartdelivery.enums.CategoryEnum;
import com.sparta.spartdelivery.enums.OrderStatusEnum;
import com.sparta.spartdelivery.enums.UserRoleEnum;
import com.sparta.spartdelivery.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class OrderServiceIntegrationTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    StoreRepository storeRepository;

    User user;
    OrderResponseDto createdOrder = null;
    Store store;

    @BeforeEach
    public void setUp() {

    }

    @Test
    @DisplayName("결제 및 주문 생성")
    @Transactional
    public void checkout_success() {
        String storePhoneNumber = "010-1234-1234";
        String storeAddress = "storeAddress";
        Integer totalPrice = 10 * 100;
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.ORDERED;
        user = new User(
                "email@email.com"
                , "1123"
                , UserRoleEnum.CLIENT
                , 10000
                , "oksu"
                , "010-0000-0000"
                , "서울시 주소"
                , 1
        );
        userRepository.save(user);

        Store store = new Store(
                "storeName",
                CategoryEnum.KOREAN,
                storePhoneNumber,
                storeAddress,
                "imageUrl",
                50,
                10,
                null,
                null)
                ;
        storeRepository.save(store);

        Menu menu = new Menu(
                1,
                store,
                "menuName",
                1000,
                "description",
                "imageUrl"
        );
        menuRepository.save(menu);

        CartItem cartItem = new CartItem(
                1,
                (short) 1,
                user,
                menu,
                store
        );
        cartItemRepository.save(cartItem);

        OrderResponseDto order = orderService.checkout(user);

        assertNotNull(order.getOrderId()); // orderId 가 존재하는지 확인
        assertEquals(order.getPhoneNumber(), storePhoneNumber);
        assertEquals(order.getAddress(), storeAddress);
        assertEquals(order.getTotalPrice(), totalPrice);
        assertEquals(order.getOrderStatus(), orderStatusEnum.getValue());

    }

    @Test
    @DisplayName("포인트가 부족한 경우")
    @Transactional
    public void checkout_point_insufficient() {
        String storePhoneNumber = "010-1234-1234";
        String storeAddress = "storeAddress";
        Integer totalPrice = 10 * 100;
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.ORDERED;

        User user = new User("email@email.com", "1123", UserRoleEnum.CLIENT, 500, // 잔액을 주문 총액보다 적게 설정
                "oksu", "010-0000-0000", "서울시 주소", 1);
        userRepository.save(user);

        store = new Store(
                "storeName",
                CategoryEnum.KOREAN,
                storePhoneNumber,
                storeAddress,
                "imageUrl",
                50,
                10,
                null,
                null)
        ;
        storeRepository.save(store);

        Menu menu = new Menu(
                1,
                store,
                "menuName",
                10001,
                "description",
                "imageUrl"
        );
        menuRepository.save(menu);

        CartItem cartItem = new CartItem(
                1,
                (short) 1,
                user,
                menu,
                store
        );

        cartItemRepository.save(cartItem);

        // 주문 생성 시도
        Exception exception = assertThrows(RuntimeException.class, () -> {
            OrderResponseDto order = orderService.checkout(user);
        });

        // 예외 메시지 확인
        String expectedMessage = "포인트가 충분하지 않습니다.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    @Transactional
    public void markOrderAsDelivered_orderDoesNotExist() {
        // 주문 ID가 존재하지 않는 경우
        Integer orderId = 1000;

        // 주문 조회 실패
        assertThrows(RuntimeException.class, () -> {
            orderService.markOrderAsDelivered(orderId);
        });

        // 예외 메시지 확인
        String expectedMessage = "주문을 찾을 수 없습니다.";
        String actualMessage = assertThrows(RuntimeException.class, () -> {
            orderService.markOrderAsDelivered(orderId);
        }).getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void markOrderAsDelivered_orderStatusNotOrdered() {
        // 주문 ID가 존재하는 경우
        Integer orderId = 1;

        // 주문 생성
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatusEnum(OrderStatusEnum.DELIVERED);
        orderRepository.save(order);

        // 주문 상태가 ORDERED가 아닌 경우
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.markOrderAsDelivered(orderId);
        });

        // 예외 메시지 확인
        String expectedMessage = "이미 배달완료된 주문 입니다.";
        String actualMessage = assertThrows(IllegalArgumentException.class, () -> {
            orderService.markOrderAsDelivered(orderId);
        }).getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
