package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.entity.CartItem;
import com.sparta.spartdelivery.entity.Store;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class CartController {


    private  List<CartItem> cartItems = Arrays.asList(
            new CartItem("Classic Cheeseburger", "yummy", 10000, 2),
            new CartItem("Veggie Delight Sandwich","yummy",  8000, 1),
            new CartItem("Grilled Salmon", "yummy", 12000, 3),
            new CartItem("Pepperoni Pizza", "yummy", 9000, 1)
    );
    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {
        double total = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

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
        return "cart";
    }


    @PostMapping("/cart/checkout")
    public String processCheckout(Model model, HttpServletRequest request) {
        List<CartItem> sessionCartItems = (List<CartItem>) request.getSession().getAttribute("cartItems");
        Double sessionTotalPrice = (Double) request.getSession().getAttribute("totalPrice");
        Store sessionStoreInfo = (Store) request.getSession().getAttribute("storeInfo");

        int orderNumber = generateOrderNumber();

        model.addAttribute("orderItems", sessionCartItems);
        model.addAttribute("totalPrice", sessionTotalPrice);
        model.addAttribute("storeInfo", sessionStoreInfo);
        model.addAttribute("orderNumber", orderNumber);
        return "checkout";
    }
    private int generateOrderNumber() {
        // 임시로 주문번호 부여하기 위함
        return (int) (Math.random() * 100000);
    }


}
