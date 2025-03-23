package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequestDTO;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

public class TestShowtimeService {
    @Mock
    private ShowtimeRepository showtimeRepository; // create a mock version of the class
    @Mock
    private MovieRepository movieRepository;
    @InjectMocks
    private ShowtimeService showtimeService; // inject the mock version

    @BeforeEach
    void setUp() { // initialize the mock
        MockitoAnnotations.openMocks(this);
    }

    /** test add showtime */

    // add showtime success test
    @Test
    @DisplayName("Should add a showtime successfully - 200")
    void addShowtimeSuccess() {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(30f, 1L, "Theater 1",
                OffsetDateTime.now().plusHours(1), OffsetDateTime.now().plusHours(3));
        // movie exists in the DB
        given(movieRepository.findById(dto.getMovieId())).willReturn(Optional.of(new Movie()));
        // there is no overlapping showtime in the DB
        given(showtimeRepository.findOverlappingShowtimes(null, "Theater 1",
                dto.getStartTime(), dto.getEndTime()))
                .willReturn(Optional.empty());
        // showtime has saved in the DB successfully
        given(showtimeRepository.save(any(Showtime.class))).willReturn(new Showtime());

        Showtime result = showtimeService.addShowtime(dto);
        // saved showtime is with values
        assertNotNull(result);
        verify(showtimeRepository).save(any(Showtime.class)); // make sure "save" is called
    }

    // add showtime with overlapping theater and time interval test
    @Test
    @DisplayName("Should throw an AlreadyExistException when adding a showtime that overlaps an existing one - 409")
    void showtimeWithOverlappingTheaterAndTimeIntervalThrowsException() {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(30f, 1L, "Theater 1",
                OffsetDateTime.now().plusHours(1), OffsetDateTime.now().plusHours(3));
        // movie exists in the DB
        given(movieRepository.findById(dto.getMovieId())).willReturn(Optional.of(new Movie()));
        // there is an overlapping showtime in the DB
        given(showtimeRepository.findOverlappingShowtimes(null, "Theater 1",
                dto.getStartTime(), dto.getEndTime()))
                .willReturn(Optional.of(new Showtime()));

        assertThrows(AlreadyExistException.class, () -> showtimeService.addShowtime(dto));
    }

    /** test get showtimes */

    // get showtime by id success test
    @Test
    @DisplayName("Should get a showtime by id successfully - 200")
    void getShowtimeByIdSuccess() {
        Long id = 1L;
        Showtime showtime = new Showtime();
        // showtime exists in the DB
        given(showtimeRepository.findById(id)).willReturn(Optional.of(showtime));

        Showtime result = showtimeService.getShowtimeById(id);
        // result is not null and is the same as the showtime
        assertNotNull(result);
        assertEquals(showtime, result);
    }

    // get showtime by id that does not exist test
    @Test
    @DisplayName("Should throw a NotFoundException when getting a showtime by id that does not exist - 404")
    void showtimeByIdNotFoundThrowsException() {
        Long id = 1L;
        // showtime does not exist in the DB
        given(showtimeRepository.findById(id)).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> showtimeService.getShowtimeById(id));
    }

    /** test update showtime */

    // update showtime success test
    @Test
    @DisplayName("Should update a showtime time successfully - 200")
    void updateShowtimeSuccess() {
        Long showtimeId = 1L;
        Showtime existingShowtime = new Showtime(showtimeId, 30f, 1L, "Theater 1",
                OffsetDateTime.now().plusHours(1), OffsetDateTime.now().plusHours(3));

        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(30f, 1L, "Theater 1",
                OffsetDateTime.now().plusHours(2), OffsetDateTime.now().plusHours(4)); // new values

        // showtime exists in the DB, movie exists in the DB,  there is no overlapping showtime
        given(showtimeRepository.findById(showtimeId)).willReturn(Optional.of(existingShowtime));
        given(movieRepository.findById(dto.getMovieId())).willReturn(Optional.of(new Movie()));
        given(showtimeRepository.findOverlappingShowtimes(showtimeId, "Theater 1",
                dto.getStartTime(), dto.getEndTime()))
                .willReturn(Optional.empty());

        // updated in the DB successfully
        showtimeService.updateShowtime(showtimeId, dto);
        // updated showtime is with the correct values
        assertEquals(existingShowtime.getStartTime(), dto.getStartTime());
        assertEquals(existingShowtime.getEndTime(), dto.getEndTime());
        verify(showtimeRepository).save(existingShowtime); // make sure "save" has been called so that the showtime was updated
    }

    // update showtime with null values test
    @Test
    @DisplayName("Should throw a NotFoundException when updating a showtime that does not exist - 404")
    void updateShowtimeNotFoundThrowsException() {
        Long showtimeId = 1L;
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(30f, 1L, "Theater 1",
                OffsetDateTime.now().plusHours(1), OffsetDateTime.now().plusHours(3));
        // showtime does not exist in the DB
        given(showtimeRepository.findById(showtimeId)).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> showtimeService.updateShowtime(showtimeId, dto));
    }

    // update showtime with invalid movie id test
    @Test
    @DisplayName("Should throw a NotFoundException when updating a showtime with an invalid movie id - 404")
    void updateShowtimeInvalidMovieIdThrowsException() {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(30f, 1L, "Theater 1",
                OffsetDateTime.now().plusHours(1), OffsetDateTime.now().plusHours(3));
        // showtime with id 1 exists in the DB, movie does not exist in the DB
        given(showtimeRepository.findById(1L)).willReturn(Optional.of(new Showtime()));
        given(movieRepository.findById(dto.getMovieId())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> showtimeService.updateShowtime(1L, dto));
    }


    /** test delete showtime */
    // delete showtime success test
    @Test
    @DisplayName("Should delete a showtime successfully - 200")
    void deleteShowtimeSuccess() {
        Showtime showtime = new Showtime();
        Long showtimeId = showtime.getId();

        // showtime exists in the DB
        given(showtimeRepository.findById(showtimeId)).willReturn(Optional.of(showtime));

        showtimeService.deleteShowtime(showtimeId);
        verify(showtimeRepository).delete(showtime); // delete has been called
    }

    // delete showtime that does not exist test
    @Test
    @DisplayName("Should throw a NotFoundException when deleting a showtime that does not exist - 404")
    void deleteShowtimeNotFoundThrowsException() {
        Long showtimeId = 100L;
        // showtime does not exist in the DB
        given(showtimeRepository.findById(showtimeId)).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> showtimeService.deleteShowtime(showtimeId));
    }

    /**
     * add showtime with null values test
     * add showtime with empty values test
     * add showtime with invalid movie id test
     * add showtime with start time after end time test
     * add showtime with time interval < movie duration test
     * add showtime with time interval = movie duration test
     * add showtime with wrong time format start time test
     *

     *
     * update showtime with empty values test
     * update showtime start time to be after end time test
     * update showtime start time == end time test
     * update showtime start time with wrong time format test
     * update showtime price to be negative test
     * update showtime theater to one with an overlapping time interval test
     *
     * delete showtime that has bookings test
     */
}
