package com.wernick.bookshelf.core.service;

import com.wernick.bookshelf.core.model.User;
import com.wernick.bookshelf.core.model.Book;
import com.wernick.bookshelf.core.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(String username, String password, String avatar) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAvatar(avatar);

        // Add starter books
        addStarterBooks(user);

        return userRepository.save(user);
    }

    private void addStarterBooks(User user) {
        List<Book> starterBooks = List.of(
            createBook("The Hobbit", "J.R.R. Tolkien", 295,
                "A hobbit's unexpected journey with dwarves to reclaim their mountain home from a dragon.",
                "A timeless fantasy adventure that started it all.", user, 0),
            createBook("1984", "George Orwell", 328,
                "A dystopian novel about surveillance, control, and the destruction of truth.",
                "A chilling warning about totalitarianism that remains relevant today.", user, 1),
            createBook("Pride and Prejudice", "Jane Austen", 432,
                "A story of love and marriage among the English gentry of the early 19th century.",
                "A masterpiece of wit and social commentary.", user, 2)
        );

        starterBooks.forEach(user::addBook);
    }

    private Book createBook(String title, String author, int pages, String summary, String review, User user, int position) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPages(pages);
        book.setSummary(summary);
        book.setReview(review);
        book.setPosition(position);
        book.setUser(user);
        return book;
    }

    public List<String> getAvailableAvatars() {
        return List.of(
            "📚", "🤓", "🦉", "🎭", "🎨",
            "🎬", "🎮", "🎧", "🎪", "🦋"
        );
    }
} 