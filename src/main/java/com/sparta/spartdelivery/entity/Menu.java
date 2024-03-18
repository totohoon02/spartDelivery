package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.dto.MenuRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String description;
    private String price;
    private String imageUrl;

    public Menu(MenuRequestDto requestDto) {
        name = requestDto.getName();
        description = requestDto.getDescription();
        price = requestDto.getPrice();
        imageUrl = requestDto.getImageUrl();
    }

    public void updateMenu(MenuRequestDto requestDto) {
        name = requestDto.getName();
        description = requestDto.getDescription();
        price = requestDto.getPrice();
        imageUrl = requestDto.getImageUrl();
    }
}
