package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.responsedto.ResValueDTO;
import com.vuviet.ThuongMai.repository.UserRepository;
import com.vuviet.ThuongMai.util.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class DashBoardController {
    private final UserRepository userRepository;

    public DashBoardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/count/users")
    @ApiMessage("Count user")
    public ResponseEntity<ResValueDTO> countUsers(){
        ResValueDTO resValueDTO = new ResValueDTO();
        resValueDTO.setValue(this.userRepository.countUsers());
        return ResponseEntity.ok(resValueDTO);
    }
}
