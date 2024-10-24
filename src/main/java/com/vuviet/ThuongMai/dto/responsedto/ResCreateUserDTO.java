package com.vuviet.ThuongMai.dto.responsedto;

import lombok.Data;

import java.time.Instant;

@Data
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Instant createdAt;

}
