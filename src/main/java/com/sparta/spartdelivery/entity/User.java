package com.sparta.spartdelivery.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private String username;
    private String phoneNumber;
    private String address;
    private int remainingPoints;
}
