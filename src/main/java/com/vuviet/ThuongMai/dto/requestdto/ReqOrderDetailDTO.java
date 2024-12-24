package com.vuviet.ThuongMai.dto.requestdto;

import com.vuviet.ThuongMai.util.constant.PaymentMethod;
import com.vuviet.ThuongMai.util.constant.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class ReqOrderDetailDTO {
    private String name;

    private String phoneNumber;

    private String address;

    private List<ItemDTO> items;

    private Long userId;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemDTO{
        private long id;

        private long quantity;
    }
}
