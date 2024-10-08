package com.example.ThuongMai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ResultPageDTO {
    private Object result;
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta{
        private long page;
        private long pageSize;
        private long totalPage;
        private long totalElement;
    }
}
