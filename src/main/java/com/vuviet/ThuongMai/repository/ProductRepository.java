package com.vuviet.ThuongMai.repository;

import com.vuviet.ThuongMai.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);

    List<Product> findByIdIn(List<Long> ids);

    @Query("SELECT p FROM Product p ORDER BY p.createdAt desc")
    List<Product> findTop5ByOrderByCreatedAtDesc();
}
