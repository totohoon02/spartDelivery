package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import lombok.Getter;

// 회원가입 Dto
@Getter
public class SignupRequestDto {
    @Email
    private String email;

    // 이메일 인증코드
    private String emailCode;

    // 필요하다면 password 복잡도 설정
    // @Pattern(regexp = "^[A-Za-z\\d@$!%*?&]{8,15}$")
    private String password;

    private String userName;
    private UserRoleEnum role;
}