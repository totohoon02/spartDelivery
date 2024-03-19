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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Assuming you have a User entity

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
    private Double totalPrice;


    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;


}
