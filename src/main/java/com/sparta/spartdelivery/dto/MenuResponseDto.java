package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {
    private String name;
    private String description;
    private String price;
    private String imageUrl;

    public MenuResponseDto(Menu menu) {
        name = menu.getName();
        description = menu.getDescription();
        price = menu.getPrice();
        imageUrl = menu.getImageUrl();
    }
}
