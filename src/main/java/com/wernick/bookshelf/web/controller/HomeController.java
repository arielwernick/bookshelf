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

import java.util.List;

@Controller
public class HomeController {

    private final BookRepository bookRepository;

    @Autowired
    public HomeController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books", bookRepository.findAllByOrderByPositionAsc());
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

    @PostMapping("/api/books/reorder")
    @ResponseBody
    public ResponseEntity<?> reorderBooks(@RequestBody List<Long> bookIds) {
        try {
            int position = 0;
            for (Long id : bookIds) {
                Book book = bookRepository.findById(id).orElseThrow();
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