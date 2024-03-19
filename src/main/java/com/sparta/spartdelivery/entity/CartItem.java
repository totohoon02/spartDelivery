package com.sparta.spartdelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "CartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    private Short quantity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "menuId", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = false)
    private Store store;


}
