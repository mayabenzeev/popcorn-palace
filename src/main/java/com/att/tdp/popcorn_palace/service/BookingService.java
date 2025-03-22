package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ShowTimeRepository showTimeRepository;

    public Booking bookTicket(Booking booking) {
        // check if wanted showtime exists
        if (!showTimeRepository.findById(booking.getShowTimeId()).isPresent()) {
            throw new NotFoundException(String.format("Show time with id %d does not exist", booking.getShowTimeId()));
        }
        // check if the wanted seat is not taken
        if (bookingRepository.findByShowTimeIdAndSeatNumber(booking.getShowTimeId(), booking.getSeatNumber()).isPresent()) {
            throw new AlreadyExistException(String.format("Seat number %d is already booked for showtime with id %d",
                    booking.getSeatNumber(), booking.getShowTimeId()));
        }
        return bookingRepository.save(booking); // save booking to db
    }
}
