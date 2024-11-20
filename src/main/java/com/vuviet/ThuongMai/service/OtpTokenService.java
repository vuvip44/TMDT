package com.vuviet.ThuongMai.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpTokenService {
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();// Bộ nhớ tạm

    private final Map<String, Long> otpExpiry = new ConcurrentHashMap<>();    // Lưu thời gian hết hạn

    @Value("${vuviet.jwt.otp-token-validity-in-seconds}")
    private long otpTokenExpiration; // 5 phút

    private final EmailService emailService;

    public OtpTokenService( EmailService emailService) {

        this.emailService = emailService;
    }

    /**
     * Sinh mã OTP và lưu trong bộ nhớ tạm
     */
    public String generateOtp(String email) {
        // Kiểm tra nếu email này đã yêu cầu OTP gần đây
        if (otpExpiry.containsKey(email) && otpExpiry.get(email) > System.currentTimeMillis()) {
            throw new IllegalStateException("Bạn đã yêu cầu OTP, vui lòng chờ!");
        }

        // Sinh mã OTP ngẫu nhiên
        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // 6 chữ số

        // Lưu OTP vào bộ nhớ tạm
        otpStorage.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + otpTokenExpiration);

        return otp;
    }

    /**
     * Xác thực mã OTP
     */
    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) {
            return false; // Không tìm thấy mã OTP cho email
        }

        if (otpExpiry.get(email) < System.currentTimeMillis()) {
            otpStorage.remove(email); // OTP đã hết hạn
            otpExpiry.remove(email);
            return false;
        }

        boolean isValid = otpStorage.get(email).equals(otp);

        // Xóa OTP sau khi xác thực thành công
        if (isValid) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
        }

        return isValid;
    }

    /**
     * Xóa OTP theo email
     */
    public void invalidateOtp(String email) {
        otpStorage.remove(email);
        otpExpiry.remove(email);
    }

    public void sendOtp(String email,String otp) {

        this.emailService.sendEmailFromTemplateSync(
                email,
                "Thư xác nhận đổi mật khẩu",
                "/email/email",
                otp);



    }
}
