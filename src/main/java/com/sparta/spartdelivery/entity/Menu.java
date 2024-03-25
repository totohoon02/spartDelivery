package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.dto.MenuRequestDto;
import lombok.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = false)
    private Store store;

    private String menuName;

    private Integer price;

    private String description;

    private String imageUrl;

    public Menu(MenuRequestDto requestDto) {
        menuName = requestDto.getMenuName();
        description = requestDto.getDescription();
        price = requestDto.getPrice();
        imageUrl = requestDto.getImageUrl();
    }

    public void updateMenu(MenuRequestDto requestDto) {
        menuName = requestDto.getMenuName();
        description = requestDto.getDescription();
        price = requestDto.getPrice();
        imageUrl = requestDto.getImageUrl();
    }
}
