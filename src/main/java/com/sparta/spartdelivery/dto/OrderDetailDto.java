package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Integer menuId;
    private String menuName;
    private String description;
    private int quantity;
    private Integer price;
}
