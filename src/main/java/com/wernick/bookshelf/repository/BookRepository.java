package com.wernick.bookshelf.repository;

import com.wernick.bookshelf.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
} 