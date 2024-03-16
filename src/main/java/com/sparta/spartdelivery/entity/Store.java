package com.sparta.spartdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Store {
    private String name;
    private String address;
    private String phoneNumber;
    private double rating;
}
