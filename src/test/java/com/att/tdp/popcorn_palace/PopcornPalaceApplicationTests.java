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


}
