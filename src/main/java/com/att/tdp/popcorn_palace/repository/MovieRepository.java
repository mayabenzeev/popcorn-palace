package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Movie entities.
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {
    /**
     * Find a movie by title.
     * @param title the title
     * @return the movie if found
     */
    Optional<Movie> findByTitle(String title);
}
