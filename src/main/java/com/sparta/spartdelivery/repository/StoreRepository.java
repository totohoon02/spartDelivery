package com.sparta.spartdelivery.repository;


import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Review> findByReviews(Store store);
}
