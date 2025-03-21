package com.att.tdp.popcorn_palace.entity;

import java.util.UUID;

public class Booking {
    Long id;
    UUID userId;
    ShowTime showTime;
    Integer seatNumber;

    public Booking() {
    }

    public Booking(UUID userId, ShowTime showtimeId, Integer seatNumber) {
        this.userId = userId;
        this.showTime = showtimeId;
        this.seatNumber = seatNumber;
    }
}
