package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.entity.Store;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class ReviewController {

    @GetMapping("/store/{storeId}/reviews/write")
    public String writeReviewPage(String storeId, Model model) {
            return "review-write";
    }


    @GetMapping("/store/{storeId}/reviews")
    public String showReviewsForStore(@PathVariable("storeId") String storeId, Model model) {
        // 이후에는 storeId로 식당 정보 가져와야함. 임시로 더미데이터 사용
        Store store = new Store("Burger Joint", "123 Burger Lane, Flavor Town", "010-1234-5678", 4.9);
        model.addAttribute("store", store);
        List<Review> reviews = Arrays.asList(
                new Review("David Johnson", "Amazing restaurant", "★★★★★", "5/5"),
                new Review("Alice Brown", "Great ambiance", "★★★★☆", "4/5"),
                new Review("Sara Miller", "Delicious dishes", "★★★★☆", "4/5"),
                new Review("Mark Wilson", "Friendly staff", "★★★★★", "5/5")
        );

        // In a real scenario, filter or fetch the reviews based on the storeId

        model.addAttribute("reviews", reviews);
        // You might also want to add store information to the model
        return "reviews"; // Name of your Thymeleaf template for store-specific reviews
    }
    @PostMapping("/submit-review")
    public String submitReview(Review review) {
        // Logic to save the review
        return "redirect:/reviews"; // Redirect to the review listing page after submission
    }

}
