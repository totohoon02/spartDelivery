package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStore(Store store);

    List<Review> findByStoreId(Long storeId);
}