package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuResponseDto {
    private Integer menuId;
    private String menuName;
    private String description;
    private Integer price;
    private String imageUrl;

    public MenuResponseDto(Menu menu) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.description = menu.getDescription();
        this.price = menu.getPrice();
        this.imageUrl = menu.getImageUrl();
    }

}
