package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Store;

public class StoreResponseDto {
    private final Long storeId;
    private final String name;
    private final String address;
    private final String phoneNumber;
    private final double rating;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.phoneNumber = store.getPhoneNumber();
        this.rating = store.getRating();
    }
}
