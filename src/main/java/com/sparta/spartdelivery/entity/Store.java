package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.enums.CategoryEnum;
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

    private String phoneNumber;

    private String address;

    private String imageUrl;

    private Integer totalRatings = 0;

    private Integer ratingsCount = 0;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public void addRating(Byte rating) {
        this.totalRatings += rating;
        this.ratingsCount++;
    }
    public double getRating() {
        if (this.ratingsCount == 0) return 0;
        return (double) this.totalRatings / this.ratingsCount;
    }
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;
}
