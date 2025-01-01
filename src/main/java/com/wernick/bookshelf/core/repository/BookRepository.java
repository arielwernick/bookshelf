package com.wernick.bookshelf.core.repository;

import com.wernick.bookshelf.core.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
} 