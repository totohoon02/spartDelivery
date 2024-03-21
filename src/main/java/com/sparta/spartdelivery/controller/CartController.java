package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.CartItemResponseDto;
import com.sparta.spartdelivery.dto.CartResponseDto;
import com.sparta.spartdelivery.dto.UpdateCartItemDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<CartItemResponseDto> addToCart(@PathVariable Integer menuId
            , @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        CartItemResponseDto cartItemResponseDto = cartService.addToCart(menuId, user);
        return ResponseEntity.ok(cartItemResponseDto);
    }

    @GetMapping
    // 장바구니 조회
    // 수정 필요: authentication 이용해서 사용자 확인 로직 적용하고 RequestParam 없애야
    public String getCartItems(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        CartResponseDto cartResponseDto = cartService.getCartItems(user);
        if (cartResponseDto ==null) {
            return "redirect:/store";
        }

        model.addAttribute("cartItems", cartResponseDto.getCartItems());
        model.addAttribute("store", cartResponseDto.getStore());
        model.addAttribute("totalPrice", cartResponseDto.getTotalPrice());
        return "cart";
    }

    @PatchMapping("/{menuId}")
    // 장바구니 수량 수정
    public ResponseEntity<?> updateCartItem(@PathVariable Integer menuId
            , @RequestBody UpdateCartItemDto updateCartItemDto
            , @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        try {
            cartService.updateCartItem(menuId, updateCartItemDto, user);
            return ResponseEntity.ok().body("Cart item quantity updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/{menuId}")
    // 특정 메뉴 삭제
    public ResponseEntity<String> deleteCartItem(@PathVariable Integer menuId
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        String message = cartService.deleteCartItem(menuId, user);
        return ResponseEntity.ok(message);
    }

//    @DeleteMapping("/clear")
//    // 장바구니 전체 삭제
//    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        User user = userDetails.getUser();
//        String message = cartService.clearCart(user);
//        return ResponseEntity.ok(message);
//    }


}
