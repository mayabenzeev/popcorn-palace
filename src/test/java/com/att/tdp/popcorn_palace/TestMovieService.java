package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.MovieRequestDTO;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

public class TestMovieService {

    @Mock
    private MovieRepository movieRepository; // create a mock version of the class

    @InjectMocks
    private MovieService movieService; // inject the mock version

    @BeforeEach
    void setUp() { // initialize the mock
        MockitoAnnotations.openMocks(this);
    }

    /** test add movie */

    // add movie success test
    @Test
    @DisplayName("Should add a movie successfully - 200")
    void addMovieSuccess() {
        MovieRequestDTO dto = new MovieRequestDTO("Avatar", "Action", 120, 8f, 2020);
        // movie does not already exist in the DB
        given(movieRepository.findByTitle("Avatar")).willReturn(Optional.empty());

        Movie savedMovie = MovieService.fromDtoToMovie(dto);
        // movie has saved in the DB successfully
        given(movieRepository.save(any(Movie.class))).willReturn(savedMovie);

        Movie result = movieService.addMovie(dto);
        // saved movie is with the correct values
        assertEquals(dto.getTitle(), result.getTitle());
        verify(movieRepository).save(any(Movie.class)); // make sure "save" has been called
    }

    // add movie with existing title test
    @Test
    @DisplayName("Should throw an AlreadyExistException when adding a movie with an existing title - 409")
    void addMovieWithExistingTitleThrowsException() {
        MovieRequestDTO dto = new MovieRequestDTO("Avatar", "Action", 120, 8f, 2020);
        // movie already exists in the DB
        given(movieRepository.findByTitle("Avatar")).willReturn(Optional.of(new Movie()));

        // make sure the exception is thrown
        assertThrows(AlreadyExistException.class, () -> movieService.addMovie(dto));
    }

    /** test get movies */

    // get all movies success test
    @Test
    @DisplayName("Should get all movies from db - 200")
    void getAllMoviesReturnList() {
        // mock the repository to return a list of movies
        given(movieRepository.findAll()).willReturn(java.util.List.of(new Movie(), new Movie()));

        // get all movies
        Iterable<Movie> result = movieService.getAllMovies();

        // result is not null and has 2 movies
        assertNotNull(result);
        assertThat(result).hasSize(2);
        verify(movieRepository).findAll(); // make sure "findAll" has been called
    }

    /** test update movies */

    // update movie success test
    @Test
    @DisplayName("Should update a movie title and duration successfully - 200")
    void updateMovieSuccess() {
        String movieTitle = "Titanic";
        Movie existingMovie = new Movie();
        existingMovie.setTitle(movieTitle);

        MovieRequestDTO dto = new MovieRequestDTO("Titanic 2", "Drama", 180, 10.f, 2024);
        // original movie exists in the DB, modified name dosent
        given(movieRepository.findByTitle(movieTitle)).willReturn(Optional.of(existingMovie));
        given(movieRepository.findByTitle(dto.getTitle())).willReturn(Optional.empty());

        // updated in the DB successfully
        movieService.updateMovie(movieTitle, dto);
        // updated movie is with the correct values
        assertThat(existingMovie.getTitle()).isEqualTo(dto.getTitle());
        verify(movieRepository).save(existingMovie); // make sure "save" has been called so that the movie was updated
    }

    // update movie not found test
    @Test
    @DisplayName("Should throw a NotFoundException when updating a movie that does not exist - 404")
    void updateMovieNotFoundThrowsException() {
        String movieTitle = "Titanic";
        MovieRequestDTO dto = new MovieRequestDTO("Titanic 2", "Drama", 180, 10.f, 2024);
        // original movie does not exist in the DB
        given(movieRepository.findByTitle(movieTitle)).willReturn(Optional.empty());

        // make sure the exception is thrown
        assertThrows(NotFoundException.class, () -> movieService.updateMovie(movieTitle, dto));
    }

    // update movie title to an existing movie title test
    @Test
    @DisplayName("Should throw an AlreadyExistException when updating a movie title to an existing movie title - 409")
    void updateMovieWithExistingTitleThrowsException() {
        MovieRequestDTO dto = new MovieRequestDTO("Lion King", "Children", 100, 9.5f, 2016);
        // original movie exists in the DB and modified name also
        given(movieRepository.findByTitle("Cinderella")).willReturn(Optional.of(new Movie()));
        given(movieRepository.findByTitle(dto.getTitle())).willReturn(Optional.of(new Movie()));

        // make sure the exception is thrown
        assertThrows(AlreadyExistException.class, () -> movieService.updateMovie("Cinderella", dto));
    }


    /** test delete movies */
    // delete movie success test
    @Test
    @DisplayName("Should delete a movie successfully - 200")
    void deleteMovieSuccess() {
        String movieTitle = "Titanic";
        Movie existingMovie = new Movie();
        existingMovie.setTitle(movieTitle);

        // original movie exists in the DB
        given(movieRepository.findByTitle(movieTitle)).willReturn(Optional.of(existingMovie));

        // deleted from the DB successfully
        movieService.deleteMovie(movieTitle);
        verify(movieRepository).delete(existingMovie);  //delete has been called
    }

    // delete movie that does not exist test
    @Test
    @DisplayName("Should throw a NotFoundException when deleting a movie that does not exist - 404")
    void deleteMovieNotFoundThrowsException() {
        String movieTitle = "Wicked";
        // original movie does not exist in the DB
        given(movieRepository.findByTitle(movieTitle)).willReturn(Optional.empty());

        // make sure the exception is thrown
        assertThrows(NotFoundException.class, () -> movieService.deleteMovie(movieTitle));
    }
}
