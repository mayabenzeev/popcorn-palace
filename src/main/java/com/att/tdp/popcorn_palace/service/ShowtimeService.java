package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequestDTO;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * Service class for managing showtimes.
 */
@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private MovieRepository movieRepository;

    /**
     * Get a showtime by its id
     * @param id - the showtime id
     * @return Showtime object
     * @throws NotFoundException if the showtime does not exist in the database
     */
    public Showtime getShowtimeById(Long id) {
        // check if showtime exists
        Optional<Showtime> dbShowtime = showtimeRepository.findById(id);

        if (!dbShowtime.isPresent()) {
            throw new NotFoundException(String.format("Showtime with id %d does not exist", id));
        }

        return dbShowtime.get();
    }

    /**
     * Add a new showtime to the database
     * @param showtimeDTO - the new showtime to add
     * @return Showtime saved entity
     * @throws NotFoundException,BadRequestException,AlreadyExistException
     */
    public Showtime addShowtime(ShowtimeRequestDTO showtimeDTO) {
        validateShowtime(showtimeDTO, null); // validate the new showtime

        Movie dbMovie = returnMovieIfExist(showtimeDTO.getMovieId()); // check if movie exists

        return showtimeRepository.save(fromDtoToShowtime(showtimeDTO, dbMovie)); // save show time to db
    }

    /**
     * Update an existing showtime in the database
     * @param showtimeId - the id of the showtime to update
     * @param updatedShowtimeDTO - the new showtime values
     * @throws NotFoundException,BadRequestException,AlreadyExistException
     */
    public void updateShowtime(Long showtimeId, ShowtimeRequestDTO updatedShowtimeDTO) {
        // check if show time exists
        Optional<Showtime> dbShowtime = showtimeRepository.findById(showtimeId);
        if (!dbShowtime.isPresent()) {
            throw new NotFoundException(String.format("Showtime with id %d does not exist", showtimeId));
        }
        // check if movie exists
        Movie dbMovie = returnMovieIfExist(updatedShowtimeDTO.getMovieId());

        validateShowtime(updatedShowtimeDTO, showtimeId); // validate the updates

        // Update show time to the new values
        Showtime existingShowtime = dbShowtime.get(); // get existing show time from the optional object
        existingShowtime.setMovie(dbMovie);
        existingShowtime.setPrice(updatedShowtimeDTO.getPrice());
        existingShowtime.setTheater(updatedShowtimeDTO.getTheater());
        existingShowtime.setStartTime(updatedShowtimeDTO.getStartTime());
        existingShowtime.setEndTime(updatedShowtimeDTO.getEndTime());

        showtimeRepository.save(existingShowtime); // save updated show time to db
    }

    /**
     * Delete a showtime from the database
     * @param id - the id of the showtime to delete
     * @throws NotFoundException if the showtime does not exist in the database
     */
    public void deleteShowtime(Long id) {
        // check if show time exists
        Optional<Showtime> dbShowtime = showtimeRepository.findById(id);

        if (!dbShowtime.isPresent()) {
            throw new NotFoundException(String.format("Showtime with id %d does not exist", id));
        }

        showtimeRepository.delete(dbShowtime.get()); // delete show time from db
    }

    /**
     * Validate showtime by checking:
     * 1. Movie exists
     * 2. The new showtime times are valid
     * 3. The new showtime does not overlap with another show time in the same theater
     * @param showtimeDTO - the showtime to validate
     * @param excludeId - the id of the showtime to exclude from the overlap check (null if not updating)
     * @throws NotFoundException,BadRequestException,AlreadyExistException
     */
    private void validateShowtime(ShowtimeRequestDTO showtimeDTO, Long excludeId) {
        Movie dbMovie = returnMovieIfExist(showtimeDTO.getMovieId());
        validateTimeInterval(showtimeDTO, dbMovie.getDuration());
        checkNoOverlaps(showtimeDTO, excludeId);
    }

    /**
     * Return the movie it is the db, otherwise throw a NotFoundException
     * @param movieId - the movie id
     * @return - Movie if exists
     * @throws NotFoundException
     */
    private Movie returnMovieIfExist(Long movieId) {
        Optional<Movie> dbMovie = movieRepository.findById(movieId);
        if (!dbMovie.isPresent()) {
            throw new NotFoundException(String.format("Movie with id %d does not exist", movieId));
        }
        return dbMovie.get();
    }

    /**
     * Validate the correction of the showtime time interval
     * (startTime before endTime, at least the movie duration)
     * @param showtimeDTO - the showtime to validate
     * @param expectedDuration - the expected duration of the movie
     * @throws BadRequestException
     */
    private void validateTimeInterval (ShowtimeRequestDTO showtimeDTO, int expectedDuration) {
        // start time is before the end time
        if (!showtimeDTO.getStartTime().isBefore(showtimeDTO.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        // showtime duration is at least the movie duration
        Duration showtimeDuration = Duration.between(showtimeDTO.getStartTime(), showtimeDTO.getEndTime());
        if (showtimeDuration.toMinutes() < expectedDuration) {
            throw new BadRequestException(String.format("Showtime duration must be at least %d minutes", expectedDuration));
        }
    }

    /**
     * Check if the new showtime does not overlap with another show time in the same theater
     * @param showtimeDTO - the showtime to validate
     * @param currentId - the id of the showtime to exclude from the overlap check (null if not updating)
     * @throws AlreadyExistException
     */
    private void checkNoOverlaps (ShowtimeRequestDTO showtimeDTO, Long currentId){
        // check if the new showtime does not overlap with another *different* show time in the same theater
        if(showtimeRepository.findOverlappingShowtimes(
                currentId, showtimeDTO.getTheater(), showtimeDTO.getStartTime(), showtimeDTO.getEndTime()).isPresent()){
            throw new AlreadyExistException(String.format(
                    "A showtime already exists in theater %s that overlaps this show time.", showtimeDTO.getTheater()));
        }
    }

    /**
     * Convert ShowtimeDTO to Showtime entity
     * @param showtimeDTO
     * @return Showtime object
     */
    public static Showtime fromDtoToShowtime(ShowtimeRequestDTO showtimeDTO, Movie movie){
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setPrice(showtimeDTO.getPrice());
        showtime.setTheater(showtimeDTO.getTheater());
        showtime.setStartTime(showtimeDTO.getStartTime());
        showtime.setEndTime(showtimeDTO.getEndTime());
        return showtime;
    }
}
