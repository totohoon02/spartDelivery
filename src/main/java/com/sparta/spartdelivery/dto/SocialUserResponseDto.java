package com.sparta.spartdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class SocialUserResponseDto {
    private String id;
    private String email;
    private String name;
    private String gender;
    private String birthday;
}