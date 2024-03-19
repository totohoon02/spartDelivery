package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    @GetMapping("/profile")
    public String viewProfile(Model model) {
        return "user-profile";
    }
}
