package com.wernick.bookshelf.core.service;

import com.wernick.bookshelf.core.model.TableSeat;
import com.wernick.bookshelf.core.model.User;
import com.wernick.bookshelf.core.repository.TableSeatRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KitchenTableService {
    private final TableSeatRepository tableSeatRepository;
    private static final int TOTAL_SEATS = 8;

    public KitchenTableService(TableSeatRepository tableSeatRepository) {
        this.tableSeatRepository = tableSeatRepository;
    }

    @PostConstruct
    public void initializeSeats() {
        if (tableSeatRepository.count() == 0) {
            for (int i = 1; i <= TOTAL_SEATS; i++) {
                tableSeatRepository.save(new TableSeat(i));
            }
        }
    }

    @Transactional
    public void assignSeat(User user, int seatNumber) {
        // First, remove user from any existing seat
        Optional<TableSeat> currentSeat = tableSeatRepository.findByUser(user);
        currentSeat.ifPresent(seat -> seat.setUser(null));

        // Then assign the new seat
        TableSeat targetSeat = tableSeatRepository.findById(seatNumber)
            .orElseThrow(() -> new RuntimeException("Invalid seat number"));

        if (targetSeat.isOccupied()) {
            throw new RuntimeException("Seat is already occupied");
        }

        targetSeat.setUser(user);
        tableSeatRepository.save(targetSeat);
    }

    @Transactional
    public void leaveSeat(User user) {
        Optional<TableSeat> currentSeat = tableSeatRepository.findByUser(user);
        currentSeat.ifPresent(seat -> {
            seat.setUser(null);
            tableSeatRepository.save(seat);
        });
    }

    public List<TableSeat> getTableStatus() {
        return tableSeatRepository.findAll();
    }

    public Optional<Integer> getUserSeat(User user) {
        return tableSeatRepository.findByUser(user)
            .map(TableSeat::getSeatNumber);
    }
} 