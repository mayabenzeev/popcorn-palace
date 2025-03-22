package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ShowtimeRepository showtimeRepository;

    public Booking bookTicket(Booking booking) {
        // check if wanted showtime exists
        if (!showtimeRepository.findById(booking.getShowtimeId()).isPresent()) {
            throw new NotFoundException(String.format("Show time with id %d does not exist", booking.getShowtimeId()));
        }
        // check if the wanted seat is not taken
        if (bookingRepository.findByShowtimeIdAndSeatNumber(booking.getShowtimeId(), booking.getSeatNumber()).isPresent()) {
            throw new AlreadyExistException(String.format("Seat number %d is already booked for showtime with id %d",
                    booking.getSeatNumber(), booking.getShowtimeId()));
        }
        return bookingRepository.save(booking); // save booking to db
    }
}
