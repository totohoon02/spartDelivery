package com.sparta.spartdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponseDto {
    private Integer userId;
    private String id;
    private String userName;
    private String userEmail;
}