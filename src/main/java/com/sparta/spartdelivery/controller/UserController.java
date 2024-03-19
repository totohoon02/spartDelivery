package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.ProfileResponseDto;
import com.sparta.spartdelivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/profile")
    public String getUserProfile(@PathVariable Long userId, Model model) {
        ProfileResponseDto profileResponseDto = userService.getUserProfile(userId);
        model.addAttribute("userProfile", profileResponseDto);
        return "user-profile";
    }
}
