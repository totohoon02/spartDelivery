package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.entity.*;
import com.sparta.spartdelivery.enums.OrderStatusEnum;
import com.sparta.spartdelivery.enums.UserRoleEnum;
import com.sparta.spartdelivery.repository.CartItemRepository;
import com.sparta.spartdelivery.repository.OrderRepository;
import com.sparta.spartdelivery.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private OrderService orderService;

    @Nested
    class checkoutTest {

        @Test
        @DisplayName("CartItems 가져오기 - 성공")
        public void getValidCartItems_Success() {
            Store store = new Store();
            Menu menu = new Menu();
            User user = new User();

            CartItem cartItem = new CartItem(
                    1,
                    (short) 1,
                    user,
                    menu,
                    store
            );
            CartItem cartItem2 = new CartItem(
                    2,
                    (short) 1,
                    user,
                    menu,
                    store
            );
            List<CartItem> cartItems = Arrays.asList(cartItem, cartItem2);
            when(cartItemRepository.findByUser(user)).thenReturn(cartItems);

            List<CartItem> result = orderService.getValidCartItems(user);
            Assertions.assertFalse(result.isEmpty()); // 결과가 비어있지 않음을 검증
            Assertions.assertEquals(cartItems.size(), result.size()); // 기대하는 크기가 맞는지 검증
        }

        @Test
        @DisplayName("CartItems 가져오기 - 실패")
        public void getValidCartItems_Failure() {
            User user = new User();

            //User 기준 CartItems 조회시 빈 배열 반환
            when(cartItemRepository.findByUser(user)).thenReturn(Collections.emptyList());
            // Exception 값 확인
            assertThrows(IllegalArgumentException.class, () -> {
                orderService.getValidCartItems(user);
            }, "장바구니가 비어있습니다.");
        }

        @Test
        @DisplayName("createOrder - 성공")
        public void createOrder_success() {
            // 모킹된 객체 준비
            User user = mock(User.class);
            CartItem cartItem = mock(CartItem.class);
            Store store = mock(Store.class);
            Menu menu = mock(Menu.class);
            when(cartItem.getStore()).thenReturn(store);
            when(cartItem.getMenu()).thenReturn(menu);
            when(cartItem.getQuantity()).thenReturn((short) 2);
            List<CartItem> cartItems = Arrays.asList(cartItem);

            // 메소드 실행
            Order result = orderService.createOrder(user, cartItems, 10000);

            // 검증: Order 객체가 예상대로 설정되었는지 확인
            assertNotNull(result);
            assertEquals(user, result.getUser());
            assertEquals(Integer.valueOf(10000), result.getTotalPrice());
            assertNotNull(result.getOrderedAt());
            assertEquals(store, result.getStore());
            assertEquals(OrderStatusEnum.ORDERED, result.getOrderStatusEnum());
            assertFalse(result.getOrderDetails().isEmpty());
            assertEquals(menu, result.getOrderDetails().get(0).getMenu());
            assertEquals(Short.valueOf((short) 2), result.getOrderDetails().get(0).getQuantity());
            // orderRepository.save 호출 확인
            Mockito.verify(orderRepository, times(1)).save(any(Order.class));
        }

        @Test
        @DisplayName("결제 시 고객 포인트 차감 - 성공")
        public void withdrawPoint_Success() {
            //given
            Integer initialPoint = 123;
            Integer totalPrice = 123;

            User user = User.builder()
                    .email("email@email.com")
                    .password("1123")
                    .role(UserRoleEnum.CLIENT)
                    .point(initialPoint)
                    .userName("oksu")
                    .build();

            // when
            Integer finalPoint = user.withdrawPoint(totalPrice);

            //then
            assertEquals(initialPoint - totalPrice, user.getPoint());
        }

        @Test
        @DisplayName("결제 시 고객 포인트 차감 - 실패")
        public void withdrawPoint_Failure() {
            Integer initialPoint = 123;
            Integer totalPrice = 124;

            User user = User.builder()
                    .email("email@email.com")
                    .password("1123")
                    .role(UserRoleEnum.CLIENT)
                    .point(initialPoint)
                    .userName("oksu")
                    .build();

            assertThrows(IllegalArgumentException.class, () -> {
                user.withdrawPoint(totalPrice);
            }, "포인트가 충분하지 않습니다.");
        }
        @Test
        @DisplayName("결제 시 고객 포인트 차감 - 성공")
        public void depositPoint_Success() {
            //given
            Integer initialPoint = 123;
            Integer totalPrice = 123;

            User user = User.builder()
                    .userId(1)
                    .password("1123")
                    .email("email@email.com")
                    .role(UserRoleEnum.BOSS)
                    .point(initialPoint)
                    .userName("oksu")
                    .address("서울시 주소")
                    .storeId(1)
                    .phoneNumber("010-0000-0000")
                    .build();

            // when
            Integer finalPoint = user.depositPoint(totalPrice);

            //then
            assertEquals(initialPoint + totalPrice, user.getPoint());
        }
    }
}
