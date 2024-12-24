package com.vuviet.ThuongMai.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String home() {
        return "home";
    }


    @RequestMapping("/home")
    public String homelogin(@AuthenticationPrincipal OidcUser oidcUser) {
        return "hello " + oidcUser.getFullName()+", email "+oidcUser.getEmail();
    }

    @GetMapping("/api/user")
    public Principal user(Principal principal) {
        return principal;
    }

//    @GetMapping("/email")
//    public String email() {
//        return "email";
//    }
}
