package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieRequestDTO;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    /**
     * Get all movies from the database
     * @return List of all movies
     */
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * Add a new movie to the database
     * @param movieDTO
     * @return Movie saved entity
     * @throws AlreadyExistException if the movie already exists
     */
    public Movie addMovie(MovieRequestDTO movieDTO) {
        // first we check if that movie is already exists (has the same title)
        Optional<Movie> dbMovie = movieRepository.findByTitle(movieDTO.getTitle());

        if (dbMovie.isPresent()) {
            throw new AlreadyExistException(String.format(
                    "Movie '%s' already exists", movieDTO.getTitle()));
        }

        return movieRepository.save(fromDtoToMovie(movieDTO)); // save movie to db
    }

    /**
     * Update an existing movie in the database
     * @param title
     * @param updatedMovieDTO
     * @throws NotFoundException if the movie does not exist in the database
     * @throws AlreadyExistException if the new title already exists in the database
     */
    public void updateMovie(String title, MovieRequestDTO updatedMovieDTO) {
        // check if movie exists
        Optional<Movie> dbMovie = movieRepository.findByTitle(title);

        if (!dbMovie.isPresent()) {
            throw new NotFoundException(String.format("Movie '%s' does not exist", title));
        }

        // check if can change the movie title (if the new title is different and not already exists)
        if (title != updatedMovieDTO.getTitle() && movieRepository.findByTitle(updatedMovieDTO.getTitle()).isPresent()) {
            throw new AlreadyExistException(String.format(
                    "Movie with a title '%s' already exists", updatedMovieDTO.getTitle()));
        }

        // Update movie to the new values
        Movie existingMovie = dbMovie.get(); // get existing movie from the optional object
        existingMovie.setTitle(updatedMovieDTO.getTitle());
        existingMovie.setGenre(updatedMovieDTO.getGenre());
        existingMovie.setDuration(updatedMovieDTO.getDuration());
        existingMovie.setRating(updatedMovieDTO.getRating());
        existingMovie.setReleaseYear(updatedMovieDTO.getReleaseYear());

        movieRepository.save(existingMovie); // save updated movie to db
    }

    /**
     * Delete a movie from the database
     * @param title
     * @throws NotFoundException if the movie does not exist in the database
     */
    public void deleteMovie(String title) {
        // check if movie exists
        Optional<Movie> dbMovie = movieRepository.findByTitle(title);

        if (!dbMovie.isPresent()) {
            throw new NotFoundException(String.format("Movie '%s' does not exist", title));
        }

        movieRepository.delete(dbMovie.get()); // delete movie from db
    }

    /**
     * Convert MovieDTO to Movie entity
     * @param movieDTO
     * @return Movie object
     */
    public static Movie fromDtoToMovie(MovieRequestDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setGenre(movieDTO.getGenre());
        movie.setDuration(movieDTO.getDuration());
        movie.setRating(movieDTO.getRating());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        return movie;
    }
}
