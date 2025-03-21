package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ShowTimeRepository showTimeRepository;

    public void bookTicket(Booking booking) {
        // check if show time exists
        if (!showTimeRepository.findById(booking.getShowTimeId()).isPresent()) {
            throw new IllegalArgumentException("Show time does not exist");
        }
        // check if this is not a duplicate booking
        if (bookingRepository.findByShowTimeIdAndSeatNumber(booking.getShowTimeId(), booking.getSeatNumber()).isPresent()) {
            throw new IllegalArgumentException("Seat is already booked");
        }
        bookingRepository.save(booking); // save booking to db
    }
}
