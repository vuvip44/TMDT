package com.vuviet.ThuongMai.dto.responsedto;

import com.vuviet.ThuongMai.util.constant.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
public class ResOrderDTO {
    private long id;

    private long totalPrice;

    private String name;

    private String phoneNumber;

    private String address;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    private OrderStatus status;

    private List<Item> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String name;
        private long quantity;
        private long price;
    }
}
