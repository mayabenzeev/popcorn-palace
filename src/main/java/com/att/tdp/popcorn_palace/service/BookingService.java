package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.BookingRequestDTO;
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

    /**
     * Creates a booking entity for a given showtime
     * @param bookingDTO
     * @return Booking saved entity
     */
    public Booking bookTicket(BookingRequestDTO bookingDTO) {
        // check if wanted showtime exists
        if (!showtimeRepository.findById(bookingDTO.getShowtimeId()).isPresent()) {
            throw new NotFoundException(String.format("Show time with id %d does not exist", bookingDTO.getShowtimeId()));
        }
        // check if the wanted seat is not taken
        if (bookingRepository.findByShowtimeIdAndSeatNumber(bookingDTO.getShowtimeId(), bookingDTO.getSeatNumber()).isPresent()) {
            throw new AlreadyExistException(String.format("Seat number %d is already booked for showtime with id %d",
                    bookingDTO.getSeatNumber(), bookingDTO.getShowtimeId()));
        }
        return bookingRepository.save(fromDtoToBooking(bookingDTO)); // save booking to db
    }

    /**
     * Convert BookingDTO to Booking entity
     * @param bookingDTO
     * @return Booking object
     */
    public static Booking fromDtoToBooking(BookingRequestDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setUserId(bookingDTO.getUserId());
        booking.setShowtimeId(bookingDTO.getShowtimeId());
        booking.setSeatNumber(bookingDTO.getSeatNumber());
        return booking;
    }
}
