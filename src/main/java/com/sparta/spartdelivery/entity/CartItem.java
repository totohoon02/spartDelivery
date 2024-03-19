package com.sparta.spartdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItem {
    private String menuName;
    private String description;
    private double price;
    private int quantity;
}