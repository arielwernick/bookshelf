package com.wernick.bookshelf.web.controller;

import com.wernick.bookshelf.core.model.User;
import com.wernick.bookshelf.core.repository.UserRepository;
import com.wernick.bookshelf.core.service.KitchenTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/kitchen-table")
public class KitchenTableController {
    private final KitchenTableService kitchenTableService;
    private final UserRepository userRepository;

    public KitchenTableController(KitchenTableService kitchenTableService, UserRepository userRepository) {
        this.kitchenTableService = kitchenTableService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showKitchenTable(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("seats", kitchenTableService.getTableStatus());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentSeat", kitchenTableService.getUserSeat(currentUser).orElse(null));
        return "kitchen-table";
    }

    @PostMapping("/seats/{seatNumber}")
    @ResponseBody
    public ResponseEntity<?> takeSeat(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int seatNumber) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            kitchenTableService.assignSeat(user, seatNumber);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/seats")
    @ResponseBody
    public ResponseEntity<?> leaveSeat(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        kitchenTableService.leaveSeat(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<?> getTableStatus() {
        return ResponseEntity.ok(kitchenTableService.getTableStatus());
    }
} 