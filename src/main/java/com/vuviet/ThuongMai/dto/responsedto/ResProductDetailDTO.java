package com.vuviet.ThuongMai.dto.responsedto;

import lombok.Data;

import java.util.List;

@Data
public class ResProductDetailDTO {
    private String name;

    private String description;

    private long priceUnit;

    private long unitInStock;

    private long sales;

    private long totalSold;

    private String imageUrl;

    private List<String> categories;

    private String brand;
}
