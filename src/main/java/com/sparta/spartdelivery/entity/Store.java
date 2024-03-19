package com.sparta.spartdelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name= "store")
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;

    @Column(nullable = false, unique = true)
    private String storeName;

    @Column
    @Enumerated(EnumType.STRING)
    private CategoryEnum categoryEnum;

    @Column
    private String phoneNumber;

    @Column
    private String address;

    @Column
    private String imageUrl;

    @Column
    private Integer totalRatings;

    @Column
    private Integer ratingsCount;

    public Store(String storeName, CategoryEnum categoryEnum, String phoneNumber, String address, String imageUrl, Integer totalRatings, Integer ratingsCount) {
        this.storeName = storeName;
        this.categoryEnum = categoryEnum;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.imageUrl = imageUrl;
        this.totalRatings = totalRatings;
        this.ratingsCount = ratingsCount;
    }
}
