package com.sparta.spartdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponseDto {
    private Long id;
    private String userName;
    private String phoneNumber;
    private String address;
    private int point;
}
