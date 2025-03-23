package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.BookingRequestDTO;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Repository class for managing bookings.
 */
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
        Showtime showtime = returnMovieIfExist(bookingDTO.getShowtimeId());
        return bookingRepository.save(fromDtoToBooking(bookingDTO, showtime)); // save booking to db
    }

    /**
     * Return the showtime it is the db, otherwise throw a NotFoundException
     * @param showtimeId - the showtime id
     * @return - Showtime if exists
     * @throws NotFoundException
     */
    private Showtime returnMovieIfExist(Long showtimeId) {
        Optional<Showtime> dbShowtime = showtimeRepository.findById(showtimeId);
        if (!dbShowtime.isPresent()) {
            throw new NotFoundException(String.format("Movie with id %d does not exist", showtimeId));
        }
        return dbShowtime.get();
    }

    /**
     * Convert BookingDTO to Booking entity
     * @param bookingDTO
     * @return Booking object
     */
    public static Booking fromDtoToBooking(BookingRequestDTO bookingDTO, Showtime showtime) {
        Booking booking = new Booking();
        booking.setUserId(bookingDTO.getUserId());
        booking.setShowtime(showtime);
        booking.setSeatNumber(bookingDTO.getSeatNumber());
        return booking;
    }
}
