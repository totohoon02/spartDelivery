package com.sparta.spartdelivery.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.spartdelivery.enums.OrderStatusEnum;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.mapping.Join;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private Integer userId;

    private Integer storeId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatusEnum;

    private LocalDateTime orderedAt;

    private LocalDateTime deliveredAt;

    private Integer totalPrice;




}
