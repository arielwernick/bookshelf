package com.wernick.bookshelf.web.controller;

import com.wernick.bookshelf.core.model.User;
import com.wernick.bookshelf.core.repository.BookRepository;
import com.wernick.bookshelf.core.repository.UserRepository;
import com.wernick.bookshelf.core.service.KitchenTableService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final KitchenTableService kitchenTableService;

    public LandingController(
            UserRepository userRepository,
            BookRepository bookRepository,
            KitchenTableService kitchenTableService) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.kitchenTableService = kitchenTableService;
    }

    @GetMapping("/")
    public String showLanding(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("bookCount", bookRepository.countByUser(user));
        model.addAttribute("currentSeat", kitchenTableService.getUserSeat(user).orElse(null));

        return "landing";
    }
} 