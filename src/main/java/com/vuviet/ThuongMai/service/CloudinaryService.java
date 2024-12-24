package com.vuviet.ThuongMai.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map upload(MultipartFile file)  {
        try{
            Map<String, Object> uploadParams = new HashMap<>();
            // Lấy tên tệp gốc và loại bỏ phần mở rộng
            String originalFilename = file.getOriginalFilename();

            // Loại bỏ phần mở rộng
            String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));

            // Tạo tên duy nhất bằng cách thêm timestamp
            String uniqueFileName = fileNameWithoutExtension + "-" + System.currentTimeMillis();


            uploadParams.put("folder", "tmdt");
            uploadParams.put("public_id", uniqueFileName); // Đặt tên cho tệp tin
            uploadParams.put("resource_type", "image");
            uploadParams.put("chunk_size", 100000000); // Kích thước chunk tối đa
            uploadParams.put("timeout", 120000); // Thời gian tối đa để tải lên
            uploadParams.put("overwrite", true);
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            return result;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }
}

