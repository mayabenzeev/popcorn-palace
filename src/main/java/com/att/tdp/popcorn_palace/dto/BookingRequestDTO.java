package com.att.tdp.popcorn_palace.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class BookingRequestDTO {

    @NotNull(message = "User ID is required")
    private UUID userId;
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;
    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be greater than 0")
    @Max(value = 1000, message = "Seats are limited to 1000 seats") // limit the seat number to 1000
    private Integer seatNumber;

    public UUID getUserId() { return this.userId; }
    public Long getShowtimeId() { return this.showtimeId; }

    public Integer getSeatNumber() { return this.seatNumber; }

    public void setUserId(UUID userId) { this.userId = userId; }
    public void setShowtimeId(Long showtimeId) { this.showtimeId = showtimeId; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }
}
