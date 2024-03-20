package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.CartAddItemRequestDto;
import com.sparta.spartdelivery.dto.CartItemResponseDto;
import com.sparta.spartdelivery.dto.CartResponseDto;
import com.sparta.spartdelivery.dto.UpdateCartItemDto;
import com.sparta.spartdelivery.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{menuId}")
    // 메뉴 장바구니에 추가
    public ResponseEntity<CartItemResponseDto> addToCart(@PathVariable Integer menuId, @RequestBody CartAddItemRequestDto cartAddItemRequestDto) {
        cartAddItemRequestDto.setMenuId(menuId);
        CartItemResponseDto cartItemResponseDto = cartService.addToCart(cartAddItemRequestDto);
        return ResponseEntity.ok(cartItemResponseDto);
    }

    @GetMapping
    // 장바구니 조회
    // 수정 필요: authentication 이용해서 사용자 확인 로직 적용하고 RequestParam 없애야
    public String getCartItems(@RequestParam Integer userId, Model model) {
        CartResponseDto cartResponseDto = cartService.getCartItems(userId);

        model.addAttribute("cartItems", cartResponseDto.getCartItems());
        model.addAttribute("store", cartResponseDto.getStore());
        model.addAttribute("totalPrice", cartResponseDto.getTotalPrice());
        return "cart";
    }

    @PatchMapping("/{menuId}")
    // 장바구니 수량 수정
    public ResponseEntity<?> updateCartItem(@RequestParam Integer userId, @PathVariable Integer menuId, @RequestBody UpdateCartItemDto updateCartItemDto) {
        try {
            cartService.updateCartItem(menuId, updateCartItemDto);
            return ResponseEntity.ok().body("Cart item quantity updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/{menuId}")
    // 특정 메뉴 삭제
    public ResponseEntity<String> deleteCartItem(@RequestParam Integer userId, @PathVariable Integer menuId) {
        String message = cartService.deleteCartItem(userId, menuId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/clear")
    // 장바구니 전체 삭제
    public ResponseEntity<String> clearCart(@RequestParam Integer userId) {
        String message = cartService.clearCart(userId);
        return ResponseEntity.ok(message);
    }


}
