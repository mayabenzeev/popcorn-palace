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
    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime; // maintain the showtime entity using the foreign key column showtime_id

    //    @Column(name = "showtime_id", nullable = false) // showtime id cannot be null
    //    private Long showtimeId; // foreign key
    @Column(name = "seat_number", nullable = false) // seat number cannot be null
    private Integer seatNumber;

    public void setUserId(UUID userId) { this.userId = userId; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }
    public UUID getId() { return this.id; }
    public UUID getUserId() { return this.userId; }
    public Showtime getShowtime() { return this.showtime; }

    public Integer getSeatNumber() { return this.seatNumber; }
}
