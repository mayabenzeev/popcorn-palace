package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.BookingRequestDTO;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.BookingService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.junit.jupiter.api.BeforeEach;

import java.awt.print.Book;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PopcornPalaceApplicationTests {
	// Add all layers integration tests

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BookingService bookingService;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private ShowtimeRepository showtimeRepository;

	@Autowired
	private MovieRepository movieRepository;

	@BeforeEach
	void cleanDatabase() {
		bookingRepository.deleteAll();
		showtimeRepository.deleteAll();
		movieRepository.deleteAll();
	}
	/**
	 * ensure the app context loads
	 */
	@Test
	void contextLoads() {
	}

	/**
	 * delete cascade test:
	 * given a movie with showtimes and bookings - when the movie is deleted,
	 * then the showtimes and bookings are also deleted
	 */
	@Test
	@Transactional
	@DisplayName("Deleting a movie should delete its showtimes and bookings")
	void deletingMovieDeletesShowtimesAndBookings() {
		// Set up a movie to persist
		Movie movie = new Movie();
		movie.setTitle("Snow White");
		movie.setGenre("Children");
		movie.setDuration(140);
		movie.setRating(9.2f);
		movie.setReleaseYear(2005);
		entityManager.persist(movie);
		entityManager.flush();
		System.out.println("Movie ID after flush = " + movie.getId());


		// Set up a showtime linked to the movie to persist
		Showtime showtime = new Showtime();
		showtime.setPrice(30.f);
		showtime.setTheater("Theater 1");
		showtime.setStartTime(OffsetDateTime.now().plusDays(1));
		showtime.setEndTime(OffsetDateTime.now().plusDays(1).plusMinutes(140));
		showtime.setMovie(movie);
		entityManager.persist(showtime);
		entityManager.flush();
		System.out.println("Movie of the showtime = " + showtime.getMovie().getId());

		// Set up a booking linked to the showtime to persist
		Booking booking = new Booking();
		booking.setUserId(UUID.randomUUID());
		booking.setShowtime(showtime);
		booking.setSeatNumber(5);
		entityManager.persist(booking);
		entityManager.flush();
		System.out.println("Showtime of the booking = " + booking.getShowtime().getId());

		// delete the movie
		entityManager.remove(movie);
		entityManager.flush();
		entityManager.clear(); // clear the persistence context to fetch the data from the database not from the cache

		// assert showtime and booking are deleted
		List<Showtime> deletedShowtime = entityManager.createQuery(
				"SELECT s FROM Showtime s WHERE s.movie.title = 'Snow White'", Showtime.class).getResultList();
		List<Booking> deletedBooking = entityManager.createQuery(
				"SELECT b FROM Booking b", Booking.class).getResultList();
		assertTrue(deletedShowtime.isEmpty());
		assertTrue(deletedBooking.isEmpty());
	}

//	/**
//	 * delete not harmful test:
//	 * given a movie with showtimes and bookings - when a movie is deleted,
//	 * then the showtimes related are deleted but other showtimes and bookings are not deleted
//	 */
//	@Test
//	@Transactional
//	@DisplayName("Deleting a movie should only delete its related showtimes and bookings only")

	/**
	 * parallel double booking test - race conditions:
	 * simulate a parallel double booking scenario: 2 users try to book the same seat at the same time
	 * then at most one booking is successful
	 */
	@Test
	@Transactional
	@Rollback
	@DisplayName("Only one of two parallel double booking should success test")
	void parallelDoubleBookingOneSuccessOneThrowsException() throws InterruptedException {
		// set up a movie to persist
		Movie movie = new Movie();
		movie.setTitle("First Movie");
		movie.setGenre("Fantasy");
		movie.setDuration(100);
		movie.setRating(9.f);
		movie.setReleaseYear(2024);
		movieRepository.save(movie);

		// set up a showtime linked to the movie to persist
		Showtime showtime = new Showtime();
		showtime.setPrice(50.f);
		showtime.setTheater("Theater 2");
		showtime.setStartTime(OffsetDateTime.now().plusDays(1));
		showtime.setEndTime(OffsetDateTime.now().plusDays(1).plusMinutes(100));
		showtime.setMovie(movie);
		Showtime savedShowtime = showtimeRepository.save(showtime);
		showtimeRepository.flush();
		entityManager.clear();  // clear persistence context

		// set up two similar booking requests
		BookingRequestDTO dto1 = new BookingRequestDTO(UUID.randomUUID(), savedShowtime.getId(), 20);
		BookingRequestDTO dto2 = new BookingRequestDTO(UUID.randomUUID(), savedShowtime.getId(), 20);
		// threadsafe parallel access list of excepions
		List<Exception> errors = Collections.synchronizedList(new ArrayList<>());

		Thread t1 = new Thread(() -> {
			try {
				bookingService.bookTicket(dto1);
			} catch (Exception e) {
				errors.add(e);
			}});
		Thread t2 = new Thread(() -> {
			try {
				bookingService.bookTicket(dto2);
			} catch (Exception e) {
				errors.add(e);
			}});

		t1.start();
		t2.start();
		t1.join();
		t2.join();
		for (Exception e : errors) {
			System.out.println(e);
		}
		// assert at least 1 booking throws an exception
		assertTrue(errors.size() >= 1 && errors.size() <= 2, "Expected at least one booking to fail");

		boolean anyAlreadyExist = errors.stream().anyMatch(e -> e instanceof AlreadyExistException);
		assertTrue(anyAlreadyExist, "Expected at least one AlreadyExistException");

	}




}
