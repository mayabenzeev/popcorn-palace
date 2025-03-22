package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.ShowTime;
import com.att.tdp.popcorn_palace.exception.AlreadyExistException;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class ShowTimeService {

    @Autowired
    private ShowTimeRepository showTimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    public ShowTime getShowTimeById(Long id) {
        // check if showtime exists
        Optional<ShowTime> dbShowTime = showTimeRepository.findById(id);

        if (!dbShowTime.isPresent()) {
            throw new NotFoundException(String.format("ShowTime with id %d does not exist", id));
        }

        return dbShowTime.get();
    }
    public ShowTime addShowTime(ShowTime showTime) {
        // check if there is no intersected time intervals in the same theater
        if(showTimeRepository.findOverlappingShowTimes(showTime.getId(), showTime.getTheater(), showTime.getStartTime(), showTime.getEndTime()).isPresent()){
            throw new AlreadyExistException(String.format(
                    "ShowTime %d in theater %s overlaps with another show time", showTime.getId(), showTime.getTheater()));
        }

        return showTimeRepository.save(showTime); // save show time to db
    }

    public void updateShowTime(Long id, ShowTime updatedShowTime) {
        // check if show time exists
        Optional<ShowTime> dbShowTime = showTimeRepository.findById(id);
        if (!dbShowTime.isPresent()) {
            throw new NotFoundException(String.format("ShowTime with id %d does not exist", id));
        }

        // check if that wanted movie id exist in the movies db
        Optional<Movie> dbMovie = movieRepository.findById(updatedShowTime.getMovieId());
        if (!dbMovie.isPresent()) {
            throw new NotFoundException(String.format("Movie with id %d does not exist", updatedShowTime.getMovieId()));
        }

        // check the correction of the time interval and overlaps showtimes
        validateTimeInterval(updatedShowTime, dbMovie.get().getDuration());
        checkNoOverlaps(updatedShowTime);

        // Update show time to the new values
        ShowTime existingShowTime = dbShowTime.get(); // get existing show time from the optional object
        existingShowTime.setMovieId(updatedShowTime.getMovieId());
        existingShowTime.setPrice(updatedShowTime.getPrice());
        existingShowTime.setTheater(updatedShowTime.getTheater());
        existingShowTime.setStartTime(updatedShowTime.getStartTime());
        existingShowTime.setEndTime(updatedShowTime.getEndTime());

        showTimeRepository.save(existingShowTime); // save updated show time to db
    }

    public void deleteShowTime(Long id) {
        // check if show time exists
        Optional<ShowTime> dbShowTime = showTimeRepository.findById(id);

        if (!dbShowTime.isPresent()) {
            throw new NotFoundException(String.format("ShowTime with id %d does not exist", id));
        }

        showTimeRepository.delete(dbShowTime.get()); // delete show time from db
    }

    private void validateTimeInterval (ShowTime showTime, int expectedDuration) {
        // Check if the start time is before the end time
        if (!showTime.getStartTime().isBefore(showTime.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        // Check if the showtime duration is at least the movie duration
        Duration showtimeDuration = Duration.between(showTime.getStartTime(), showTime.getEndTime());
        if (showtimeDuration.toMinutes() < expectedDuration) {
            throw new BadRequestException(String.format("Show time duration must be at least %d minutes", expectedDuration));
        }
    }

    private void checkNoOverlaps (ShowTime showTime) {
        // check if the new showtime does not overlap with another *different* show time in the same theater
        if(showTimeRepository.findOverlappingShowTimes(
                showTime.getId(), showTime.getTheater(), showTime.getStartTime(), showTime.getEndTime()).isPresent()){
            throw new AlreadyExistException(String.format(
                    "A showtime already exists in theater %s that overlaps this show time.", showTime.getTheater()));
        }

    }
}
