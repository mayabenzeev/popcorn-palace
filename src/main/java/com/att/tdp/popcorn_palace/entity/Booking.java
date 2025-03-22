package com.att.tdp.popcorn_palace.entity;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"showtime_id", "seat_number"})}) // to prevent a seat from being booked twice for the same showtime
public class Booking {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false) // user id cannot be null
    private UUID userId;

    // foreign key
    @Column(name = "showtime_id", nullable = false) // showtime id cannot be null
    private Long showtimeId;
    @Column(name = "seat_number", nullable = false) // seat number cannot be null
    private Integer seatNumber;

    public void setUserId(UUID userId) { this.userId = userId; }
    public void setShowtimeId(Long showtimeId) { this.showtimeId = showtimeId; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

    public UUID getUserId() { return this.userId; }
    public Long getShowtimeId() { return this.showtimeId; }
    public Integer getSeatNumber() { return this.seatNumber; }

}
