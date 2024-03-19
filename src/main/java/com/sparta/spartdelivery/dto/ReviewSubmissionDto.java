package com.sparta.spartdelivery.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.sparta.spartdelivery.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSubmissionDto {
    private String comment;
    private Byte rating;
    private Integer userId;
}
