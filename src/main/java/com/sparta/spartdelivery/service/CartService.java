package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.CartResponseDto;
import com.sparta.spartdelivery.dto.UpdateCartItemDto;
import com.sparta.spartdelivery.entity.CartItem;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.repository.CartItemRepository;
import com.sparta.spartdelivery.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MenuRepository menuRepository;

    public CartService(CartItemRepository cartItemRepository, MenuRepository menuRepository) {
        this.cartItemRepository = cartItemRepository;
        this.menuRepository = menuRepository;
    }

    // 장바구니에 메뉴 추가
    @Transactional
    public String addToCart(Integer menuId, User user) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));
        Store store = menu.getStore();

        List<CartItem> existingCartItems = cartItemRepository.findByUser(user);
        if (!existingCartItems.isEmpty() && !existingCartItems.get(0).getStore().equals(store)) {
            // 다른 가게의 메뉴가 이미 장바구니에 있음
            return "다른 가게의 메뉴가 장바구니에 담겨있습니다. 장바구니를 초기화 하시겠습니까?";
        }

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
         return "메뉴가 성공적으로 장바구니에 추가되었습니다.";
    }

    // 장바구니 조회
    public CartResponseDto getCartItems(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        return CartResponseDto.from(cartItems, user);
    }

    // 장바구니 메뉴 수량 수정
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
    public String clearCart(User user) {
        // 사용자 ID로 모든 CartItems 조회 후 삭제
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(cartItems);
        return "장바구니가 비워졌습니다.";
    }
}
