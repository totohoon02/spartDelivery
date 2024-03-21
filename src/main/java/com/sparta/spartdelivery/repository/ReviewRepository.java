package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByStore_storeId(Integer storeId);

    Review findByUser_userId(Integer userId);
}
