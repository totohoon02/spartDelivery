package com.sparta.spartdelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JoinController {

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/create-store")
    public String createStore(){
        return "store_new";
    }
    @GetMapping("/update-store")
    public String updateStore(){
        return "store_update";
    }
    @GetMapping("/create-menu")
    public String createMenu(){
        return "menu_new";
    }
    @GetMapping("/update-menu")
    public String updateMenu(){
        return "menu_update";
    }

}
