package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponseDto {
    private Integer userId;
    private String userName;
    private String phoneNumber;
    private String address;
    private Integer point;
}
