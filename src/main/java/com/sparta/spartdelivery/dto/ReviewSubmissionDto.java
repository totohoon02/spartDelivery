package com.sparta.spartdelivery.dto;

import lombok.Getter;

@Getter
public class ReviewSubmissionDto {
    private final String comment;
    private final Byte rating;

    public ReviewSubmissionDto(String comment, Byte rating) {
        this.comment = comment;
        this.rating = rating;
    }
}
