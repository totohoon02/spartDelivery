package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {
    private final String menuName;
    private final String description;
    private final Integer price;
    private final String imageUrl;

    public MenuResponseDto(Menu menu) {
        menuName = menu.getMenuName();
        description = menu.getDescription();
        price = menu.getPrice();
        imageUrl = menu.getImageUrl();
    }
}
