package com.att.tdp.popcorn_palace.entity;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"showtime_id", "seat_number"})}) // to prevent a seat from being booked twice for the same showtime
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //JPA auto generated - leave it null
    @Column(name = "user_id", nullable = false) // user id cannot be null
    private UUID userId;
    //TODO: change to String (?)

    // foreign key
    @Column(name = "showtime_id", nullable = false) // showtime id cannot be null
    private Long showTimeId;
    @Column(name = "seat_number", nullable = false) // seat number cannot be null
    private Integer seatNumber;

    public Long getShowTimeId() {
        return this.showTimeId;
    }

    public Integer getSeatNumber() {
        return this.seatNumber;
    }
}
