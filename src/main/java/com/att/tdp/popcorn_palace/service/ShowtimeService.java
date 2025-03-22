package com.att.tdp.popcorn_palace.service;

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
 * The type Showtime service.
 */
@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Showtime getShowtimeById(Long id) {
        // check if showtime exists
        Optional<Showtime> dbShowtime = showtimeRepository.findById(id);

        if (!dbShowtime.isPresent()) {
            throw new NotFoundException(String.format("Showtime with id %d does not exist", id));
        }

        return dbShowtime.get();
    }
    public Showtime addShowtime(Showtime showtime) {
        validateShowtime(showtime, null); // validate the new showtime

        return showtimeRepository.save(showtime); // save show time to db
    }

    public void updateShowtime(Long showtimeId, Showtime updatedShowtime) {
        // check if show time exists
        Optional<Showtime> dbShowtime = showtimeRepository.findById(showtimeId);
        if (!dbShowtime.isPresent()) {
            throw new NotFoundException(String.format("Showtime with id %d does not exist", showtimeId));
        }

        validateShowtime(updatedShowtime, showtimeId); // validate the updates

        // Update show time to the new values
        Showtime existingShowtime = dbShowtime.get(); // get existing show time from the optional object
        existingShowtime.setMovieId(updatedShowtime.getMovieId());
        existingShowtime.setPrice(updatedShowtime.getPrice());
        existingShowtime.setTheater(updatedShowtime.getTheater());
        existingShowtime.setStartTime(updatedShowtime.getStartTime());
        existingShowtime.setEndTime(updatedShowtime.getEndTime());

        showtimeRepository.save(existingShowtime); // save updated show time to db
    }


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
     * @param showtime - the showtime to validate
     * @param excludeId - the id of the showtime to exclude from the overlap check (null if not updating)
     * @throws NotFoundException,BadRequestException,AlreadyExistException
     */
    private void validateShowtime(Showtime showtime, Long excludeId) {
        Movie dbMovie = returnMovieIfExist(showtime.getMovieId());
        validateTimeInterval(showtime, dbMovie.getDuration());
        checkNoOverlaps(showtime, excludeId);
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
     * @param showtime - the showtime to validate
     * @param expectedDuration - the expected duration of the movie
     * @throws BadRequestException
     */
    private void validateTimeInterval (Showtime showtime, int expectedDuration) {
        // start time is before the end time
        if (!showtime.getStartTime().isBefore(showtime.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        // showtime duration is at least the movie duration
        Duration showtimeDuration = Duration.between(showtime.getStartTime(), showtime.getEndTime());
        if (showtimeDuration.toMinutes() < expectedDuration) {
            throw new BadRequestException(String.format("Showtime duration must be at least %d minutes", expectedDuration));
        }
    }

    /**
     * Check if the new showtime does not overlap with another show time in the same theater
     * @param showtime - the showtime to validate
     * @param currentId - the id of the showtime to exclude from the overlap check (null if not updating)
     * @throws AlreadyExistException
     */
    private void checkNoOverlaps (Showtime showtime, Long currentId){
        // check if the new showtime does not overlap with another *different* show time in the same theater
        if(showtimeRepository.findOverlappingShowtimes(
                currentId, showtime.getTheater(), showtime.getStartTime(), showtime.getEndTime()).isPresent()){
            throw new AlreadyExistException(String.format(
                    "A showtime already exists in theater %s that overlaps this show time.", showtime.getTheater()));
        }

    }
}
