package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Store;

public class StoreResponseDto {
    private final Integer storeId;
    private final String name;
    private final String address;
    private final String phoneNumber;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.name = store.getStoreName();
        this.address = store.getAddress();
        this.phoneNumber = store.getPhoneNumber();
    }
}
