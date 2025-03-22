package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies") // base URL for all endpoints in movies controller
public class MoviesController {

    // TODO: change to response entity upon exception handling
    private final MovieService movieService;

    @Autowired
    public MoviesController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    @PostMapping("/update/{title}")
    public void updateMovie(@PathVariable String title, @RequestBody Movie updatedMovie) {
        movieService.updateMovie(title, updatedMovie);
    }

    @DeleteMapping("/{title}")
    public void deleteMovie(@PathVariable String title) {
        movieService.deleteMovie(title);
    }


}
