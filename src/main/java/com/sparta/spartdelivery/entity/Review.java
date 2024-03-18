package com.sparta.spartdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Review {
    private String reviewerName;
    private String comment;
    private String rating;
    private String score;
}
