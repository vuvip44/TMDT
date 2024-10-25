package com.vuviet.ThuongMai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Không được để trống tên")
    private String name;

    @NotBlank(message = "Không được để trống apiPath")
    private String apiPath;

    @NotBlank(message = "Không được để trống method")
    private String method;

    @NotBlank(message = "Không được để trống module")
    private String module;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

    public Permission(String name, String apiPath, String method, String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }

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
