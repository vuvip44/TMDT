package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.responsedto.StringResponseDTO;
import com.vuviet.ThuongMai.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloudinary/upload")
@RequiredArgsConstructor
public class CloudinaryImageUploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<StringResponseDTO> uploadImage(@RequestParam("image") MultipartFile file){
        Map data = this.cloudinaryService.upload(file);
        String url=data.get("url").toString();
        StringResponseDTO responseDTO=new StringResponseDTO();
        responseDTO.setMessage(url);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
