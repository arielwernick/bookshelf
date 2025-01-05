package com.wernick.bookshelf.core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TABLE_SEATS")
public class TableSeat {
    @Id
    @Column(name = "seat_number")
    private Integer seatNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public TableSeat() {}

    public TableSeat(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOccupied() {
        return user != null;
    }
} 