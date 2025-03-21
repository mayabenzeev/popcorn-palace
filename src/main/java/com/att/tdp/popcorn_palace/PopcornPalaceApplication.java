package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PopcornPalaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PopcornPalaceApplication.class, args);
	}
	@Bean
	CommandLineRunner run(MovieRepository movieRepository) {
		return args -> {
			Movie movie = new Movie();
			movie.setTitle("Test Movie");
			movie.setGenre("Test");
			movie.setDuration(120);
			movie.setRating(6.1f);
			movie.setReleaseYear(2001);
			movieRepository.save(movie);
		};
	}

}
