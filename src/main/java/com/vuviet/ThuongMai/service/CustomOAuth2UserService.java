//package com.vuviet.ThuongMai.service;
//
//import com.vuviet.ThuongMai.entity.User;
//import com.vuviet.ThuongMai.util.SecurityUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private SecurityUtil securityUtil;
//
//
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oauth2User = super.loadUser(userRequest);
//
//
//        String email = oauth2User.getAttribute("email");
//        String name = oauth2User.getAttribute("name");
//
//        // Kiểm tra xem người dùng đã tồn tại trong database chưa
//        User userLogin = userService.getByUsername(email);
//        if (userLogin == null) {
//            // Tạo tài khoản mới
//            User user = new User();
//            user.setEmail(email);
//            user.setName(name);
//            // ... các thông tin khác
//            user = userService.createUser(user);
//        }
//
//
//
//        return oauth2User;
//    }
//}
