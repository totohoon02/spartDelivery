package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class MainController {
    @GetMapping()
    public String home(){
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/business")
    public String navigateBusiness(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        Optional<Integer> storeId = Optional.ofNullable(user.getStoreId());
        if (storeId.isPresent()) {
            return "redirect:/store/update-store";
        }
        return "redirect:/store/create-store";
    }


}
