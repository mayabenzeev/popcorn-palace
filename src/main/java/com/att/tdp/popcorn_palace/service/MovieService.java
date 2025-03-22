package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie) {
        // first we check if that movie is already exists (has the same title)
        Optional<Movie> dbMovie = movieRepository.findByTitle(movie.getTitle());

        if (dbMovie.isPresent()) {
            throw new AlreadyExistException(String.format(
                    "Movie %s already exists", movie.getTitle()));
        }

        return movieRepository.save(movie); // save movie to db
    }

    public void updateMovie(String title, Movie updatedMovie) {
        // check if movie exists
        Optional<Movie> dbMovie = movieRepository.findByTitle(title);

        if (!dbMovie.isPresent()) {
            throw new NotFoundException(String.format("Movie %s does not exist", title));
        }

        // check if can change the movie title (if the new title is not already exists)
        if (movieRepository.findByTitle(updatedMovie.getTitle()).isPresent()) {
            throw new AlreadyExistException(String.format(
                    "Movie with a title %s already exists", updatedMovie.getTitle()));
        }

        // Update movie to the new values
        Movie existingMovie = dbMovie.get(); // get existing movie from the optional object
        existingMovie.setTitle(updatedMovie.getGenre());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setDuration(updatedMovie.getDuration());
        existingMovie.setRating(updatedMovie.getRating());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());

        movieRepository.save(existingMovie); // save updated movie to db
    }

    public void deleteMovie(String title) {
        // check if movie exists
        Optional<Movie> dbMovie = movieRepository.findByTitle(title);

        if (!dbMovie.isPresent()) {
            throw new NotFoundException(String.format("Movie %s does not exist", title));
        }

        movieRepository.delete(dbMovie.get()); // delete movie from db
    }
}
