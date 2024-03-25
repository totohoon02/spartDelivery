package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.CartResponseDto;
import com.sparta.spartdelivery.dto.UpdateCartItemDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 메뉴를 장바구니에 추가
    @PostMapping("/{menuId}")
    public ResponseEntity<?> addToCart(@PathVariable Integer menuId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        String result = cartService.addToCart(menuId, user);

        if ("다른 가게의 메뉴가 장바구니에 담겨있습니다. 장바구니를 초기화 하시겠습니까?".equals(result)) {
            // 이 경우 프론트엔드에 특정 메시지와 함께 다른 처리를 위한 상태 코드를 반환
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        // 정상적으로 메뉴가 장바구니에 추가된 경우
        return ResponseEntity.ok("메뉴가 성공적으로 장바구니에 추가되었습니다.");
    }

    // 장바구니 조회
    @GetMapping
    public String getCartItems(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        CartResponseDto cartResponseDto = cartService.getCartItems(user);

        if (cartResponseDto.getCartItems().isEmpty()) {
            model.addAttribute("emptyCartMessage", "장바구니가 비어있습니다.");
        } else{
            model.addAttribute("cartItems", cartResponseDto.getCartItems());
            model.addAttribute("store", cartResponseDto.getStore());
            model.addAttribute("totalPrice", cartResponseDto.getTotalPrice());

        }
        return "cart";
    }

    // 장바구니 수량 수정
    @PatchMapping("/{menuId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Integer menuId,
                                            @RequestBody UpdateCartItemDto updateCartItemDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        cartService.updateCartItem(menuId, updateCartItemDto, user);
        return ResponseEntity.ok().body("장바구니의 수량이 성공적으로 변경되었습니다.");

    }

    // 특정 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Integer menuId
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        String message = cartService.deleteCartItem(menuId, user);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/clear")
    // 장바구니 전체 삭제
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        String message = cartService.clearCart(user);
        return ResponseEntity.ok(message);
    }


}
