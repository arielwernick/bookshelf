package com.wernick.bookshelf.web.controller;

import com.wernick.bookshelf.core.model.Book;
import com.wernick.bookshelf.core.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final BookRepository bookRepository;

    @Autowired
    public HomeController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "home";
    }

    @PostMapping("/api/books")
    @ResponseBody
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList());
        }
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }
} 