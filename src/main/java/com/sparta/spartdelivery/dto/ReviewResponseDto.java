package com.sparta.spartdelivery.dto;

import com.sparta.spartdelivery.entity.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {
    private Integer reviewId;
    private String userName;
    private String comment;
    private Byte rating;
    private Integer storeId;


    public static ReviewResponseDto convertReviewToDto(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .userName(review.getUser().getUserName())
                .comment(review.getComment())
                .rating(review.getRating())
                .storeId(review.getStore().getStoreId())
                .build();
    }
}
