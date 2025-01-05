package com.wernick.bookshelf.core.repository;

import com.wernick.bookshelf.core.model.Book;
import com.wernick.bookshelf.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByUserOrderByPositionAsc(User user);
    Optional<Book> findByIdAndUser(Long id, User user);
} 