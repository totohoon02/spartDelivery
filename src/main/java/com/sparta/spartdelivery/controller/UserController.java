package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.LoginRequestDto;
import com.sparta.spartdelivery.dto.ProfileResponseDto;
import com.sparta.spartdelivery.dto.SignupRequestDto;
import com.sparta.spartdelivery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequiredArgsConstructor
//@RequestMapping("")
//public class UserController {
//
//    private final UserService userService;
//
//    // 회원가입
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
//        try {
//            userService.signup(requestDto);
//            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: " + e.getMessage());
//        }
//    }
//
//    // 유저 프로
//    @GetMapping("/{userId}/profile")
//    public String getUserProfile(@PathVariable Integer userId, Model model) {
//        ProfileResponseDto profileResponseDto = userService.getUserProfile(userId);
//        model.addAttribute("userProfile", profileResponseDto);
//        return "user-profile";
//    }
//}


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        try {
            userService.signup(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: " + e.getMessage());
        }
    }

    // 유저 프로
    @GetMapping("/{userId}/profile")
    public String getUserProfile(@PathVariable Integer userId, Model model) {
        ProfileResponseDto profileResponseDto = userService.getUserProfile(userId);
        model.addAttribute("userProfile", profileResponseDto);
        return "user-profile";
    }

    // 메인 페이지
    @GetMapping("/")
    public String home(){
        return "redirect:/login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }
}
