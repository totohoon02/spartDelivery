package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatusEnum;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime orderedAt;

    private LocalDateTime deliveredAt;

    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id", referencedColumnName = "storeId")
    private Store store;

    public void markOrderAsDelivered(OrderStatusEnum orderStatusEnum, LocalDateTime deliveredAt) {
        this.orderStatusEnum = orderStatusEnum;
        this.deliveredAt = deliveredAt;
    }
}
