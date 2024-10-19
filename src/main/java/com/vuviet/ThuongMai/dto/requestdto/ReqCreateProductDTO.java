package com.vuviet.ThuongMai.dto.requestdto;

import com.vuviet.ThuongMai.util.constant.TypeDiscount;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ReqCreateProductDTO {
    private long id;

    @NotBlank(message = "Không được để trống tên")
    private String name;

    private String description;

    @Min(value = 1000, message = "Giá tiền phải lớn hơn 1000")
    private long priceUnit;

    @Min(value = 1, message = "Số lượng phải lớn hơn 1")
    private long quantity;

    private TypeDiscount typeDiscount;

    private long discount;

    private long brandId;

    @NotBlank(message = "Không được để trống link ảnh")
    private String imageUrl;

    private List<Long> categoryIds;
}
