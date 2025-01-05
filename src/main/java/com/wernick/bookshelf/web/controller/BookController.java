package com.wernick.bookshelf.web.controller;

import com.wernick.bookshelf.core.model.Book;
import com.wernick.bookshelf.core.model.User;
import com.wernick.bookshelf.core.repository.BookRepository;
import com.wernick.bookshelf.core.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookController(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showBooks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        List<Book> books = bookRepository.findAllByUserOrderByPositionAsc(user);
        model.addAttribute("books", books);
        model.addAttribute("user", user);
        return "books";
    }

    @PostMapping
    @ResponseBody
    public Book addBook(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Book book) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        book.setUser(user);
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Book updateBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody Book updatedBook) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Book book = bookRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new RuntimeException("Book not found"));
            
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setPages(updatedBook.getPages());
        book.setSummary(updatedBook.getSummary());
        book.setReview(updatedBook.getReview());
        book.setPosition(updatedBook.getPosition());
        
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Book book = bookRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new RuntimeException("Book not found"));
            
        bookRepository.delete(book);
    }
} 