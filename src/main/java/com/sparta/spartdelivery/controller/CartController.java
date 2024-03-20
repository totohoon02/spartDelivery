package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.CartItemResponseDto;
import com.sparta.spartdelivery.dto.CartResponseDto;
import com.sparta.spartdelivery.dto.UpdateCartItemDto;
import com.sparta.spartdelivery.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{menuId}")
    // 메뉴 장바구니에 추가
    public ResponseEntity<?> addToCart(@PathVariable("menuId") Integer menuId) {
        CartItemResponseDto cartItemResponseDto = cartService.addToCart(menuId);
        return ResponseEntity.ok(cartItemResponseDto);
    }

    @GetMapping
    // 장바구니 조회
    // 수정 필요: authentication 이용해서 사용자 확인 로직 적용하고 RequestParam 없애야
    public String getCartItems(Model model) {
        CartResponseDto cartResponseDto = cartService.getCartItems();

        model.addAttribute("cartItems", cartResponseDto.getCartItems());
        model.addAttribute("store", cartResponseDto.getStore());
        model.addAttribute("totalPrice", cartResponseDto.getTotalPrice());
        model.addAttribute("userId", cartResponseDto.getUserId());
        return "cart";
    }

    @PatchMapping("/{menuId}")
    // 장바구니 수량 수정
    public ResponseEntity<?> updateCartItem(@PathVariable Integer menuId, @RequestBody UpdateCartItemDto updateCartItemDto) {
        try {
            cartService.updateCartItem(menuId, updateCartItemDto);
            return ResponseEntity.ok().body("Cart item quantity updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/{menuId}")
    // 특정 메뉴 삭제
    public ResponseEntity<String> deleteCartItem(@PathVariable Integer menuId) {
        String message = cartService.deleteCartItem(menuId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/clear")
    // 장바구니 전체 삭제
    public ResponseEntity<String> clearCart() {
        String message = cartService.clearCart();
        return ResponseEntity.ok(message);
    }


}
