package com.wernick.bookshelf.core.repository;

import com.wernick.bookshelf.core.model.TableSeat;
import com.wernick.bookshelf.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TableSeatRepository extends JpaRepository<TableSeat, Integer> {
    Optional<TableSeat> findByUser(User user);
} 