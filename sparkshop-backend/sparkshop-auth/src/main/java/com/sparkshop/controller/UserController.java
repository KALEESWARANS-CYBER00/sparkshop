package com.sparkshop.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/profile")
    public Object profile(@AuthenticationPrincipal String email) {
        // returns the email from token subject; in real app fetch user from DB
        return java.util.Map.of("email", email);
    }

    @GetMapping("/admin/secret")
    public Object adminOnly() {
        return java.util.Map.of("secret", "only for admin");
    }
}
