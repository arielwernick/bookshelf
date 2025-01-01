package com.wernick.bookshelf.web.controller;

import com.wernick.bookshelf.core.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
} 