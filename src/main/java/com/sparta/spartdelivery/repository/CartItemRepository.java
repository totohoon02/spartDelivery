package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.CartItem;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

    List<CartItem> findByUserId(Long userId);
//    List<CartItem> findByUser_Id(Long userId);

    Optional<CartItem> findByUserAndMenu(User user, Menu menu);

    Optional<CartItem> findByUserIdAndMenuId(Long userId, Long menuId);
//    Optional<CartItem> findByUser_IdAndMenu_Id(Long userId, Long menuId);
}
