package com.example.ThuongMai.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_details")
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    private long price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
