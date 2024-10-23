package com.vuviet.ThuongMai.repository;

import com.vuviet.ThuongMai.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {
    List<OrderDetail> findByOrderId(Long orderId);

    List<OrderDetail> findByIdIn(List<Long> ids);
}
