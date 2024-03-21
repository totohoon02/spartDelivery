package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.CartItem;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndMenu(User user, Menu menu);

    Optional<CartItem> findByUser_userIdAndMenu_menuId(int i, Integer menuId);

    void deleteByUser(User user);
}
