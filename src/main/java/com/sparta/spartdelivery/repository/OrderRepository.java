package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.Order;
import com.sparta.spartdelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByStore_storeId(Integer storeId);
}
