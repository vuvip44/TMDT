package com.vuviet.ThuongMai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "brands")
@Data
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Không được để trống tên")
    private String name;

    private boolean active;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @OneToMany(mappedBy = "brand",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;

    @NotBlank(message = "Không được để trống link ảnh")
    private String urlAvatar;

//    @PrePersist
//    public void handleBeforeCreate() {
//        this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent()==true?
//                SecurityUtil.getCurrentUserLogin().get():null;
//        this.createdAt = Instant.now();
//    }
//
//    @PreUpdate
//    public void handleBeforeUpdate() {
//        this.updatedBy=SecurityUtil.getCurrentUserLogin().isPresent()==true?
//                SecurityUtil.getCurrentUserLogin().get():null;
//        this.updatedAt = Instant.now();
//    }
}
