package com.wernick.bookshelf.web.controller;

import com.wernick.bookshelf.core.model.Book;
import com.wernick.bookshelf.core.model.User;
import com.wernick.bookshelf.core.repository.BookRepository;
import com.wernick.bookshelf.core.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public HomeController(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        model.addAttribute("user", user);
        model.addAttribute("books", bookRepository.findAllByUserOrderByPositionAsc(user));
        return "home";
    }

    @PostMapping("/api/books")
    @ResponseBody
    public ResponseEntity<?> createBook(@AuthenticationPrincipal UserDetails userDetails,
                                      @Valid @RequestBody Book book,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList());
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        book.setUser(user);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }

    @PostMapping("/api/books/reorder")
    @ResponseBody
    public ResponseEntity<?> reorderBooks(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody List<Long> bookIds) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

            int position = 0;
            for (Long id : bookIds) {
                Book book = bookRepository.findByIdAndUser(id, user)
                    .orElseThrow(() -> new RuntimeException("Book not found or unauthorized"));
                book.setPosition(position++);
                bookRepository.save(book);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 