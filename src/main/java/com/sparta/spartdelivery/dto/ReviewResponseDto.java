package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {
    private Long reviewId;
    private String userName;
    private String comment;
    private byte rating;
    private Long storeId;
}
