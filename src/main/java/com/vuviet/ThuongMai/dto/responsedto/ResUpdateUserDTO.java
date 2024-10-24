package com.vuviet.ThuongMai.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Instant updatedAt;
    private RoleUser role;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleUser{
        private long id;
        private String name;
    }
}
