package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.requestdto.LoginDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResCreateUserDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResLoginDTO;
import com.vuviet.ThuongMai.entity.User;
import com.vuviet.ThuongMai.service.UserService;
import com.vuviet.ThuongMai.util.SecurityUtil;
import com.vuviet.ThuongMai.util.annotation.ApiMessage;
import com.vuviet.ThuongMai.util.error.IdInValidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

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

    @GetMapping("/auth/account")
    @ApiMessage("get account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount(){
        String email=SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get():null;
        User user=this.userService.getByUsername(email);
        ResLoginDTO.UserLogin userLogin=new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount account=new ResLoginDTO.UserGetAccount();
        if(user!=null) {
            userLogin.setId(user.getId());
            userLogin.setEmail(user.getEmail());
            userLogin.setName(user.getName());
            userLogin.setRole(user.getRole());
            account.setUser(userLogin);
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name="refresh_token", defaultValue = "abc") String refreshToken
    ) throws IdInValidException {
        if(refreshToken.equals("abc")) {
            throw new IdInValidException("Không có refresh token ở cookie");
        }

        //check valid
        Jwt decodedToken=this.securityUtil.checkValidRefreshToken(refreshToken);
        String email=decodedToken.getSubject();

        User userDB=this.userService.getUserByRefreshTokenAndEmail(refreshToken,email);
        if(userDB==null) {
            throw new IdInValidException("Refresh token không hợp lệ");
        }

        ResLoginDTO res=new ResLoginDTO();
        User currentUser=this.userService.getByUsername(email);
        if(currentUser!=null) {
            ResLoginDTO.UserLogin userLogin=new ResLoginDTO.UserLogin(currentUser.getId(),currentUser.getEmail(),currentUser.getName(),currentUser.getRole());
            res.setUser(userLogin);
        }

        //Tao moi token ms
        String access_token=this.securityUtil.createAccessToken(email,res);
        res.setAccessToken(access_token);

        String refresh_token=this.securityUtil.createRefreshToken(email,res);

        //Cap nhat lai refresh_token
        this.userService.updateUserToken(refresh_token, email);

        //set cookie
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

    @PostMapping("/auth/logout")
    @ApiMessage("Logout user")
    public ResponseEntity<Void> logout() throws IdInValidException {
        String email=SecurityUtil.getCurrentUserLogin().isPresent()?
                SecurityUtil.getCurrentUserLogin().get():null;
        if(email.equals("")) {
            throw new IdInValidException("Access token không hợp lệ");
        }

        this.userService.updateUserToken(null, email);

        ResponseCookie resCookies=ResponseCookie.from("refresh_token",null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,resCookies.toString()).body(null);
    }

    @PostMapping("/auth/register")
    @ApiMessage("Register user")
    public ResponseEntity<ResCreateUserDTO> registerUser(@RequestBody @Valid User userDTO) throws IdInValidException{
        if(this.userService.isEmailExist(userDTO.getEmail())){
            throw new IdInValidException("Email "+userDTO.getEmail()+" đã tồn tại");
        }

        String hashPassword=this.passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(hashPassword);

        User createUser=this.userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(createUser));
    }

    @GetMapping("/auth/token")
    @ApiMessage("Get access token from google")
    public ResponseEntity<ResLoginDTO> getAccessToken(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();
//        String id=attributes.get("id").toString();// hoặc bất kỳ thông tin nào khác mà Google cung cấp

        //set thong tin nguoi dung dang nhap vao context(co the su dung sau nay)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Tìm user theo email
        User currentUser = this.userService.getByUsername(email);

        // Nếu user không tồn tại, tạo mới
        if (currentUser == null) {
            String encodedPassword = passwordEncoder.encode(email);
            currentUser = new User();
            currentUser.setEmail(email);
            currentUser.setName(name);
            currentUser.setPassword(encodedPassword);

             // Đặt role mặc định cho user mới
            currentUser = this.userService.createUser(currentUser); // Lưu user mới vào database
        }



        // Khởi tạo ResLoginDTO với thông tin user
        ResLoginDTO res = new ResLoginDTO();
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getName(),
                currentUser.getRole()
        );
        res.setUser(userLogin);

        // Tạo access token
        String access_token = this.securityUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);

        // Tạo refresh token và lưu vào database
        String refresh_token = this.securityUtil.createRefreshToken(email, res);
        this.userService.updateUserToken(refresh_token, email);

        // Thiết lập cookie cho refresh token
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        // Trả về phản hồi với thông tin user và access token
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }
}
