package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.dto.StoreRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    private String storeName;
    private String address;
    private String phoneNumber;
    private double rating = 0.0d;


    public Store(StoreRequestDto requestDto) {
        this.storeName = requestDto.getStoreName();
        this.address = requestDto.getStoreAddress();
        this.phoneNumber = requestDto.getPhoneNumber();
    }

    public Store(String storeName, String address, String phoneNumber, double rating) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
    }

    public void updateStore(StoreRequestDto requestDto) {
        storeName = requestDto.getStoreName();
        address = requestDto.getStoreAddress();
        phoneNumber = requestDto.getPhoneNumber();
    }
}
