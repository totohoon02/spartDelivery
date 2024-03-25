package com.sparta.spartdelivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    private short quantity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "menuId", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = false)
    private Store store;

    public CartItem( Short quantity, User user, Menu menu, Store store) {
        this.quantity = quantity;
        this.user = user;
        this.menu = menu;
        this.store = store;
    }

    public void addQuantity(short addQuantity) {
        this.quantity += addQuantity;
    }
}
