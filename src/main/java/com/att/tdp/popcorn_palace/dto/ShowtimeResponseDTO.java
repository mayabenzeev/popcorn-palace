package com.att.tdp.popcorn_palace.dto;

import com.att.tdp.popcorn_palace.entity.Showtime;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object for showtime responses.
 */
public class ShowtimeResponseDTO {
    private Long id;
    private Float price;
    private Long movieId;
    private String theater;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    public ShowtimeResponseDTO(Showtime showtime) {
        this.id = showtime.getId();
        this.price = showtime.getPrice();
        this.movieId = showtime.getMovie().getId();
        this.theater = showtime.getTheater();
        this.startTime = showtime.getStartTime();
        this.endTime = showtime.getEndTime();
    }

    /** getters */
    public Long getId() { return this.id; }
    public Float getPrice() { return this.price;}
    public Long getMovieId() { return this.movieId; }
    public String getTheater() {return this.theater; }
    public OffsetDateTime getStartTime() { return this.startTime; }
    public OffsetDateTime getEndTime() {return this.endTime;}
}
