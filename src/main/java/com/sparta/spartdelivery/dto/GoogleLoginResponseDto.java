package com.sparta.spartdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginResponseDto {

    private String id;
    private String name;
    private String email;
    private String picture;
}