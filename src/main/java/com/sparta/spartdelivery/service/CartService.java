package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.CartAddItemRequestDto;
import com.sparta.spartdelivery.dto.CartItemResponseDto;
import com.sparta.spartdelivery.dto.CartResponseDto;
import com.sparta.spartdelivery.dto.UpdateCartItemDto;
import com.sparta.spartdelivery.entity.CartItem;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.repository.CartItemRepository;
import com.sparta.spartdelivery.repository.MenuRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import com.sparta.spartdelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private CartItemRepository cartItemRepository;
    private StoreRepository storeRepository;

    private UserRepository userRepository;
    private MenuRepository menuRepository;

    public CartService(CartItemRepository cartItemRepository, StoreRepository storeRepository,
                       UserRepository userRepository, MenuRepository menuRepository) {
        this.cartItemRepository = cartItemRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
    }

    @Autowired
    public CartService(CartItemRepository cartItemRepository, UserRepository userRepository, MenuRepository menuRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
    }

    // 장바구니에 메뉴 추가
    public CartItemResponseDto addToCart(Integer menuId, User user) {

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found"));

        Store store = menu.getStore();

        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndMenu(user, menu);
        CartItem cartItem;
        // 동일한 메뉴 추가 시 수량만 업데이트
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.addQuantity((short) 1);
        } else {
            cartItem = new CartItem((short) 1, user, menu, store);
        }
        cartItemRepository.save(cartItem);

        CartItemResponseDto responseDto = new CartItemResponseDto();
        responseDto.setMenuId(menu.getMenuId());
        responseDto.setQuantity(cartItem.getQuantity());
        responseDto.setMenuName(menu.getMenuName());

        return responseDto;
    }

    // 장바구니 조회
    public CartResponseDto getCartItems(User user) {

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        // 장바구니 물건이 없을 경우
        if (cartItems.isEmpty()) {
            return null;
        }

        CartResponseDto cartResponse = new CartResponseDto();
        List<CartItemResponseDto> cartItemDtos = new ArrayList<>();
        Integer totalPrice = 0;

        Store store = cartItems.get(0).getStore();

        for (CartItem cartItem : cartItems) {
            CartItemResponseDto cartItemDto = new CartItemResponseDto(cartItem);
            cartItemDtos.add(cartItemDto);
            totalPrice += cartItemDto.getPrice() * cartItemDto.getQuantity();
        }

        cartResponse.setCartItems(cartItemDtos);
        cartResponse.setTotalPrice(totalPrice);
        cartResponse.setStore(store);
        cartResponse.setUserId(user.getUserId());
        return cartResponse;
    }

    @Transactional
    public void updateCartItem(Integer menuId, UpdateCartItemDto updateCartItemDto, User user) {

        // 유저 인증 추가 필요
        CartItem cartItem = cartItemRepository.findByUser_userIdAndMenu_menuId(user.getUserId(), menuId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found for menuId: " + menuId + " and userId: " + user.getUserId()));

        Short updatedQuantity = updateCartItemDto.getQuantity();

        // If the quantity is less than or equal to 0, remove the item from the cart
        if (updatedQuantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            // Otherwise, update the quantity of the existing cart item
            cartItem.setQuantity(updatedQuantity);
//            cartItemRepository.save(cartItem);
        }
    }

    // 장바구니에서 아이템 삭제
    public String deleteCartItem(Integer menuId, User user) {
        Optional<CartItem> cartItem = cartItemRepository.findByUser_userIdAndMenu_menuId(user.getUserId(), menuId);
        if (cartItem.isPresent()) {
            cartItemRepository.delete(cartItem.get());
            return "해당 상품이 장바구니에서 삭제되었습니다.";
        } else {
            return "장바구니에서 해당 상품을 찾을 수 없습니다.";
        }
    }

    // 장바구니 전체 삭제
//    public String clearCart(User user) {
//        // 사용자 ID로 모든 CartItems 조회 후 삭제
//        List<CartItem> cartItems = cartItemRepository.findByUser(user);
//        cartItemRepository.deleteAll(cartItems);
//        return "장바구니가 비워졌습니다.";
//    }
}
