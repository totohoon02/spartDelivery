package com.sparta.spartdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSubmissionDto {
    private String comment;
    private Byte rating;


}
