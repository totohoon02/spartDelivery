package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.entity.CartItem;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem("Classic Cheeseburger", 8.99, 2),
                new CartItem("Veggie Delight Sandwich", 7.50, 1),
                new CartItem("Grilled Salmon", 12.99, 3),
                new CartItem("Pepperoni Pizza", 9.99, 1)
        );
        double total = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", total);
        return "cart";
    }
}
