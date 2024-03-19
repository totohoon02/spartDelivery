package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Store;
import lombok.Getter;

@Getter
public class GetStoreResponseDto {
    private Integer storeId;
    private String storeName;
    private String categoryValue;
    private String imageUrl;

    public GetStoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.categoryValue = store.getCategoryEnum().getValue();
        this.imageUrl = store.getImageUrl();
    }
}
