package com.sparta.spartdelivery.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "OrderDetail")
public class OrderDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "orderId",  nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "menuId", nullable = false)
    private Menu menu;

    private int quantity;

}
