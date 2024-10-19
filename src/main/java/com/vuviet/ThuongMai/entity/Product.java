package com.vuviet.ThuongMai.entity;


import com.vuviet.ThuongMai.util.constant.TypeDiscount;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import jakarta.validation.constraints.Min;
import lombok.Data;


import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private long priceUnit;

    private long unitInStock;

    private long totalSol=0;

    @Enumerated(EnumType.STRING)
    private TypeDiscount typeDiscount;

    @Min(value = 0)
    private long discount=0;

    private long sales;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"products"})
    @JoinTable(name="category_product",joinColumns = @JoinColumn(name = "category_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Category> categories;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private String imageUrl;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}
