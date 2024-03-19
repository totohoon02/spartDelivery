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
    public ResponseEntity<CartItemResponseDto> addToCart(@PathVariable Long menuId, @RequestBody CartAddItemRequestDto cartAddItemRequestDto) {
        cartAddItemRequestDto.setMenuId(menuId);
        CartItemResponseDto cartItemResponseDto = cartService.addToCart(cartAddItemRequestDto);
        return ResponseEntity.ok(cartItemResponseDto);
    }

    @GetMapping
    // 장바구니 조회
    // 수정 필요: authentication 이용해서 사용자 확인 로직 적용하고 RequestParam 없애야
    public String getCartItems(@RequestParam Long userId, Model model) {
        CartResponseDto cartResponseDto = cartService.getCartItems(userId);

        model.addAttribute("cartItems", cartResponseDto.getCartItems());
        model.addAttribute("store", cartResponseDto.getStore());
        model.addAttribute("totalPrice", cartResponseDto.getTotalPrice());
        return "cart";
    }

    @PatchMapping("/{menuId}")
    // 장바구니 수량 수정
    public ResponseEntity<?> updateCartItem(@RequestParam Long userId, @PathVariable Long menuId, @RequestBody UpdateCartItemDto updateCartItemDto) {
        try {
            cartService.updateCartItem(menuId, updateCartItemDto);
            return ResponseEntity.ok().body("Cart item quantity updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/{menuId}")
    // 특정 메뉴 삭제
    public ResponseEntity<String> deleteCartItem(@RequestParam Long userId, @PathVariable Long menuId) {
        String message = cartService.deleteCartItem(userId, menuId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/clear")
    // 장바구니 전체 삭제
    public ResponseEntity<String> clearCart(@RequestParam Long userId) {
        String message = cartService.clearCart(userId);
        return ResponseEntity.ok(message);
    }


//    private  List<CartItem> cartItems = Arrays.asList(
//            new CartItem("Classic Cheeseburger", "yummy", 10000, 2),
//            new CartItem("Veggie Delight Sandwich","yummy",  8000, 1),
//            new CartItem("Grilled Salmon", "yummy", 12000, 3),
//            new CartItem("Pepperoni Pizza", "yummy", 9000, 1)
//    );
//    @GetMapping("/cart")
//    public String viewCart(Model model, HttpServletRequest request) {
//        double total = cartItems.stream()
//                .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                .sum();
//
//        Store storeInfo = new Store("Burger Joint", "123 Burger Lane, Flavor Town", "010-1234-5678", 4.9);
//
//        // Storing cart data in the session
//        request.getSession().setAttribute("cartItems", cartItems);
//        request.getSession().setAttribute("totalPrice", total);
//        request.getSession().setAttribute("storeInfo", storeInfo);
//
//        model.addAttribute("cartItems", cartItems);
//        model.addAttribute("totalPrice", total);
//        model.addAttribute("storeInfo", storeInfo);
//        return "cart";
//    }
//
//
//    @PostMapping("/cart/checkout")
//    public String processCheckout(Model model, HttpServletRequest request) {
//        List<CartItem> sessionCartItems = (List<CartItem>) request.getSession().getAttribute("cartItems");
//        Double sessionTotalPrice = (Double) request.getSession().getAttribute("totalPrice");
//        Store sessionStoreInfo = (Store) request.getSession().getAttribute("storeInfo");
//
//        int orderNumber = generateOrderNumber();
//
//        model.addAttribute("orderItems", sessionCartItems);
//        model.addAttribute("totalPrice", sessionTotalPrice);
//        model.addAttribute("storeInfo", sessionStoreInfo);
//        model.addAttribute("orderNumber", orderNumber);
//        return "checkout";
//    }
//    private int generateOrderNumber() {
//        // 임시로 주문번호 부여하기 위함
//        return (int) (Math.random() * 100000);
//    }


}
