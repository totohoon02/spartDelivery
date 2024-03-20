package com.sparta.spartdelivery.service;

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
    @Transactional
    public CartItemResponseDto addToCart(Integer menuId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        Menu menu = menuRepository.findById(Long.valueOf(menuId))
                .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        Store store = menu.getStore();

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        // Check if there are items in the cart from other stores
//        boolean differentStoreExists = cartItems.stream()
//                .anyMatch(item -> !item.getStore().equals(store));
//
//        if (differentStoreExists) {
//                throw new CartConflictException("Cart contains items from a different store");
//        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndMenu(user, menu);
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity((short) (cartItem.getQuantity() + 1));
        } else {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setMenu(menu);
            cartItem.setStore(store);
            cartItem.setQuantity((short) 1);
        }
        cartItemRepository.save(cartItem);

        CartItemResponseDto responseDto = new CartItemResponseDto();
        responseDto.setMenuId(menu.getMenuId());
        responseDto.setQuantity(cartItem.getQuantity());
        responseDto.setMenuName(menu.getMenuName());

        return responseDto;
    }

    public CartResponseDto getCartItems() {
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
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        List<CartItem> cartItems = cartItemRepository.findByUser_userId(user.getUserId());
        if (cartItems.isEmpty()) {
            return new CartResponseDto();
        }

        CartResponseDto cartResponse = new CartResponseDto();
        List<CartItemResponseDto> cartItemDtos = new ArrayList<>();
        Integer totalPrice = 0;

        Store store = cartItems.get(0).getStore();

        for (CartItem cartItem : cartItems) {
            CartItemResponseDto cartItemDto = new CartItemResponseDto();
            cartItemDto.setMenuId(cartItem.getMenu().getMenuId());
            cartItemDto.setMenuName(cartItem.getMenu().getMenuName());
            cartItemDto.setPrice(cartItem.getMenu().getPrice());
            cartItemDto.setDescription(cartItem.getMenu().getDescription());
            cartItemDto.setQuantity(cartItem.getQuantity());

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
    public void updateCartItem(Integer menuId, UpdateCartItemDto updateCartItemDto) {

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
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        // 유저 인증 추가 필요
        CartItem cartItem = cartItemRepository.findByUser_userIdAndMenu_menuId(user.getUserId(), menuId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found for menuId: " + menuId + " and userId: " + 1));

        Short updatedQuantity = updateCartItemDto.getQuantity();

        // If the quantity is less than or equal to 0, remove the item from the cart
        if (updatedQuantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            // Otherwise, update the quantity of the existing cart item
            cartItem.setQuantity(updatedQuantity);
            cartItemRepository.save(cartItem);
        }
    }

    // 장바구니에서 아이템 삭제
    public String deleteCartItem(Integer menuId) {
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
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        Optional<CartItem> cartItem = cartItemRepository.findByUser_userIdAndMenu_menuId(user.getUserId(), menuId);
        if (cartItem.isPresent()) {
            cartItemRepository.delete(cartItem.get());
            return "해당 상품이 장바구니에서 삭제되었습니다.";
        } else {
            return "장바구니에서 해당 상품을 찾을 수 없습니다.";
        }
    }

    // 장바구니 전체 삭제
    public String clearCart() {
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
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        // 사용자 ID로 모든 CartItems 조회 후 삭제
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(cartItems);
        return "장바구니가 비워졌습니다.";
    }
}

