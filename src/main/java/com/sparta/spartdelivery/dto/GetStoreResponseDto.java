package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Store;
import lombok.Getter;

@Getter
public class GetStoreResponseDto {
    private final Integer storeId;
    private final String storeName;
    private final String categoryValue;
    private final String imageUrl;

    public GetStoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.categoryValue = store.getCategoryEnum().getValue();
        this.imageUrl = store.getImageUrl();
    }
}
