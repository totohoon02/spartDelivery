package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Store;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {
    private Integer reviewId;
    private String userName;
    private String comment;
    private Byte rating;
    private Integer storeId;
}
