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
        User customer = User.builder()
                .email("email@email.com")
                .password("1123")
                .role(UserRoleEnum.CLIENT)
                .point(10000)
                .address("서울시 주소")
                .phoneNumber("010-0000-0000")
                .userName("oksu")
                .build();
        userRepository.save(customer);

        Store store = Store
                .builder()
                .storeName("storeName")
                .categoryEnum(CategoryEnum.KOREAN)
                .address(storeAddress)
                .phoneNumber(storePhoneNumber)
                .imageUrl("imageUrl")
                .totalRatings(50)
                .ratingsCount(10)
                .build();

        storeRepository.save(store);

        User boss = User.builder()
                .email("boss@email.com")
                .password("1123")
                .role(UserRoleEnum.BOSS)
                .point(0)
                .address("서울시 주소")
                .phoneNumber("010-0000-0000")
                .userName("boss3")
                .storeId(store.getStoreId())
                .build();
        userRepository.save(boss);

        Menu menu = Menu.builder()
                .store(store)
                .menuName("menuName")
                .price(1000)
                .build();

        menuRepository.save(menu);

        CartItem cartItem = new CartItem(
                (short) 1,
                customer,
                menu,
                store
        );
        cartItemRepository.save(cartItem);

        OrderResponseDto order = orderService.checkout(customer);

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
        Integer insufficientPoint = 500;
        Integer totalPrice = 10 * 100;
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.ORDERED;

        User user = User.builder()
                .email("email@email.com")
                .password("1123")
                .role(UserRoleEnum.CLIENT)
                .point(insufficientPoint)
                .userName("Oksu1")
                .build();
        userRepository.save(user);

        store = Store.builder().storeName("storeName")
                .categoryEnum(CategoryEnum.KOREAN)
                .phoneNumber(storePhoneNumber)
                .address(storeAddress)
                .build()
        ;

        storeRepository.save(store);

        Menu menu = Menu.builder()
                .store(store)
                .menuName("menuName")
                .price(10001)
                .build();
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
                // 주문 생성
        Order order = Order.builder()
                .orderStatusEnum(OrderStatusEnum.DELIVERED)
                .build();
        Order createdOrder = orderRepository.save(order);


        // 주문 상태가 ORDERED가 아닌 경우
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.markOrderAsDelivered(createdOrder.getOrderId());
        });

        // 예외 메시지 확인
        String expectedMessage = "이미 배달완료된 주문 입니다.";
        String actualMessage = assertThrows(IllegalArgumentException.class, () -> {
            orderService.markOrderAsDelivered(createdOrder.getOrderId());
        }).getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
