package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Builder.Default
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
