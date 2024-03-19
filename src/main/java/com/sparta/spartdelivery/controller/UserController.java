package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.LoginRequestDto;
import com.sparta.spartdelivery.dto.SignupRequestDto;
import com.sparta.spartdelivery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
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
}