package com.vuviet.ThuongMai.dto.responsedto;

import com.vuviet.ThuongMai.util.constant.TypeDiscount;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
public class ResCreateProductDTO {
    private long id;

    private String name;

    private String description;

    private long priceUnit;

    private long unitInStock;

    private long totalSold;

    private TypeDiscount typeDiscount;

    private long discount;

    private long sales;

    private String imageUrl;

    private BrandProduct brandProduct;

    private List<CategoryProduct> categoryProducts;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandProduct {
        private long id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryProduct {
        private long id;
        private String name;
    }
}
