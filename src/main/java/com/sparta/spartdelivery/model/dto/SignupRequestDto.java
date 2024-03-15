package com.sparta.spartdelivery.model.dto;

import com.sparta.spartdelivery.model.enumtype.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @Email
    private String email;

    //@Pattern(regexp = "^[A-Za-z\\d@$!%*?&]{8,15}$")
    private String password;

    private String username;
    private UserRoleEnum role;
}