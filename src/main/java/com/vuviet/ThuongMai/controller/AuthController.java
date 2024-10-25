package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.requestdto.LoginDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResLoginDTO;
import com.vuviet.ThuongMai.entity.User;
import com.vuviet.ThuongMai.service.UserService;
import com.vuviet.ThuongMai.util.SecurityUtil;
import com.vuviet.ThuongMai.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Value("${vuviet.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    @ApiMessage("login user")
    public ResponseEntity<ResLoginDTO> login(@RequestBody @Valid LoginDTO loginDTO){
        //nap username va password vao security
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword());

        //xac thuc nguoi dung, can loadUserByUsername
        Authentication authentication=this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //set thong tin nguoi dung dang nhap vao context(co the su dung sau nay)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res=new ResLoginDTO();
        User currentUser=this.userService.getByUsername(loginDTO.getUsername());
        if(currentUser!=null){
            ResLoginDTO.UserLogin userLogin=new ResLoginDTO.UserLogin(currentUser.getId(),currentUser.getEmail(),currentUser.getName(),currentUser.getRole());
            res.setUser(userLogin);
        }

        //create token
        String access_token=this.securityUtil.createAccessToken(authentication.getName(),res);
        res.setAccessToken(access_token);

        //create refresh token
        String refresh_token=this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

        //update user with refreshToken
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

        //set cookies
        ResponseCookie resCookies=ResponseCookie.from("refresh_token",refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,resCookies.toString())
                .body(res);
    }
}
