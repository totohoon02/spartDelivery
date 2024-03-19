package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    @GetMapping("/profile")
    public String viewProfile(Model model) {
//        User userProfile = new User(
//                "User123",             // Username
//                "010-1234-5678",       // Phone Number
//                "123 Main St, Cityville, 12345", // Address
//                250                    // Remaining Points
//        );

//        model.addAttribute("userProfile", userProfile);
        return "user-profile";
    }
}
