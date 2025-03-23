package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Booking entities.
 */
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    /**
     * Find a booking by showtime id and seat number.
     *
     * @param showTimeId the showtime id
     * @param seatNumber the seat number
     * @return the booking if found
     */
    Optional<Booking> findByShowtimeIdAndSeatNumber(Long showTimeId, Integer seatNumber);
}
