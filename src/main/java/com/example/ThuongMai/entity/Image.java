package com.example.ThuongMai.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "images")
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;

    @OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY)
    private User user;

    @OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY)
    private Brand brand;

    @OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
