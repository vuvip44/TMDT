package com.vuviet.ThuongMai.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Không được để trống username")
    private String username;

    @NotBlank(message = "Không được để trống password")
    private String password;
}
