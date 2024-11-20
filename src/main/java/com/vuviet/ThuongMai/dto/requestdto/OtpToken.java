package com.vuviet.ThuongMai.dto.requestdto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OtpToken {
    private String otp; // Mã OTP
    private LocalDateTime expiryTime; // Thời gian hết hạn

    public OtpToken(String otp, LocalDateTime expiryTime) {
        this.otp = otp;
        this.expiryTime = expiryTime;
    }

    public String getOtp() {
        return otp;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}
