package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.enums.CategoryEnum;
import com.sparta.spartdelivery.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Store")
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

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;

    public void addRating(Byte rating) {
        this.totalRatings += rating;
        this.ratingsCount++;
    }
    public double getRating() {
        if (this.ratingsCount == 0) return 0;
        return (double) this.totalRatings / this.ratingsCount;
    }
    public Store(StoreRequestDto requestDto) {
        this.storeName = requestDto.getStoreName();
        this.categoryEnum = requestDto.getCategory();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.address = requestDto.getStoreAddress();
        this.ratingsCount = 0;
        this.totalRatings = 0;
    }

    public Store(String storeName, CategoryEnum categoryEnum, String phoneNumber, String address, String imageUrl, Integer totalRatings, Integer ratingsCount, List<Review> reviews, List<Menu> menus) {
        this.storeName = storeName;
        this.categoryEnum = categoryEnum;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.imageUrl = imageUrl;
        this.totalRatings = totalRatings;
        this.ratingsCount = ratingsCount;
        this.reviews = reviews;
        this.menus = menus;
    }

    public void updateStore(StoreRequestDto requestDto) {
        this.storeName = requestDto.getStoreName();
        this.categoryEnum = requestDto.getCategory();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.address = requestDto.getStoreAddress();
    }
}
