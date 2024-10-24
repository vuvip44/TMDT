package com.vuviet.ThuongMai.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vuviet.ThuongMai.dto.responsedto.ResCreateUserDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResUpdateUserDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResUserDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.User;
import com.vuviet.ThuongMai.service.UserService;
import com.vuviet.ThuongMai.util.annotation.ApiMessage;
import com.vuviet.ThuongMai.util.error.IdInValidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a user")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody @Valid User user) throws IdInValidException {
        if(this.userService.isEmailExist(user.getEmail())) {
            throw new IdInValidException("Email "+user.getEmail()+" đã tồn tại");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User createdUser = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(createdUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInValidException {
        if(this.userService.getUserById(id) == null) {
            throw new IdInValidException("Id "+id+" không tồn tại");
        }
        this.userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody @Valid User user) throws IdInValidException {
        User userUpdated = this.userService.updateUser(user);
        if(userUpdated == null) {
            throw new IdInValidException("User id "+user.getId()+" không tồn tại");
        }
        return ResponseEntity.ok(this.userService.convertToResUpdateToUserDTO(user));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInValidException {
        User user = this.userService.getUserById(id);
        if(user == null) {
            throw new IdInValidException("User id"+id+" không tồn tại");
        }
        return ResponseEntity.ok(this.userService.convertToResUserDTO(user));
    }

    @GetMapping("/users")
    @ApiMessage("Get all users")
    public ResponseEntity<ResultPageDTO> getAllUsers(
            @Filter Specification<User> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.userService.getAllUsers(spec, pageable));
    }
}
