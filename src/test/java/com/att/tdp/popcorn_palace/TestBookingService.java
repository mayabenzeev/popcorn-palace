package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.BookingRequestDTO;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

public class TestBookingService {

    @Mock
    private BookingRepository bookingRepository; // create a mock version of the class
    @Mock
    private ShowtimeRepository showtimeRepository;
    @InjectMocks
    private BookingService bookingService; // inject the mock version

    @BeforeEach
    void setUp() { // initialize the mock
        MockitoAnnotations.openMocks(this);
    }

    /** test add booking */
    // add booking success test
    @Test
    @DisplayName("Should add a booking successfully - 200")
    void addBookingSuccess() {
        BookingRequestDTO dto = new BookingRequestDTO(UUID.randomUUID(), 1L, 30);
        // there is a showtime with this id in the DB
        given(showtimeRepository.findById(dto.getShowtimeId())).willReturn(Optional.of(new Showtime()));
        // booking to the show and seat number does not exist in the DB
        given(bookingRepository.findByShowtimeIdAndSeatNumber(dto.getShowtimeId(), dto.getSeatNumber())).willReturn(Optional.empty());

        Booking savedBooking = BookingService.fromDtoToBooking(dto);
        // booking has saved in the DB successfully
        given(bookingRepository.save(any(Booking.class))).willReturn(savedBooking);

        Booking result = bookingService.bookTicket(dto);

        // saved booking is with the correct values
        assertEquals(dto.getUserId(), result.getUserId());
        assertEquals(dto.getShowtimeId(), result.getShowtimeId());
        assertEquals(dto.getSeatNumber(), result.getSeatNumber());
        verify(bookingRepository).save(any(Booking.class)); //make sure "save" has been called
    }

    // add booking with a showtime that does not exist test
    @Test
    @DisplayName("Should throw a NotFoundException when adding a booking with a showtime that does not exist - 404")
    void showtimeNotFoundThrowsException() {
        BookingRequestDTO dto = new BookingRequestDTO(UUID.randomUUID(), 10L, 2);
        // there is no showtime with this id in the DB
        given(showtimeRepository.findById(dto.getShowtimeId())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.bookTicket(dto));
    }

    // add booking to a showtime with a taken seat test
    @Test
    @DisplayName("Should throw a AlreadyExistException when adding a booking to a showtime with a taken seat - 409")
    void seatAlreadyTakenThrowsException() {
        BookingRequestDTO dto = new BookingRequestDTO(UUID.randomUUID(), 1L, 2);
        // there is a showtime with this id
        given(showtimeRepository.findById(dto.getShowtimeId())).willReturn(Optional.of(new Showtime()));
        // booking to the show and seat number already exists in the DB
        given(bookingRepository.findByShowtimeIdAndSeatNumber(dto.getShowtimeId(), dto.getSeatNumber()))
                .willReturn(Optional.of(new Booking()));

        assertThrows(AlreadyExistException.class, () -> bookingService.bookTicket(dto));
    }

}
