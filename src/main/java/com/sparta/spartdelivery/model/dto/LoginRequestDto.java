package com.sparta.spartdelivery.model.dto;

import lombok.Getter;

// 로그인 Dto
@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}