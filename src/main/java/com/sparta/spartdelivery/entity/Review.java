package com.sparta.spartdelivery.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = false)
    private Store store;

    private String comment;

    private Byte rating;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    // 리뷰 수정을 위한 메소드
    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void updateRating(Byte rating) {
        this.rating = rating;
    }
}
