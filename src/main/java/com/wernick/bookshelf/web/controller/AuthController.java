package com.wernick.bookshelf.web.controller;

import com.wernick.bookshelf.core.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("avatars", userService.getAvailableAvatars());
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String avatar) {
        try {
            userService.registerUser(username, password, avatar);
            return "success";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
} 