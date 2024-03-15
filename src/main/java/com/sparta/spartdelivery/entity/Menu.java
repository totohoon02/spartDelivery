package com.sparta.spartdelivery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Menu {
    private String id;
    private String name;
    private String price;
    private String imageUrl;
}
