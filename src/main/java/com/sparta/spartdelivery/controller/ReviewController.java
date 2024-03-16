package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.entity.Review;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class ReviewController {

    @GetMapping("/reviews")
    public String showReviews(Model model) {
        List<Review> reviews = Arrays.asList(
                new Review("David Johnson", "Amazing restaurant", "★★★★★", "5/5"),
                new Review("Alice Brown", "Great ambiance", "★★★★☆", "4/5"),
                new Review("Sara Miller", "Delicious dishes", "★★★★☆", "4/5"),
                new Review("Mark Wilson", "Friendly staff", "★★★★★", "5/5")
        );

        model.addAttribute("reviews", reviews);
        return "reviews";
    }
}
