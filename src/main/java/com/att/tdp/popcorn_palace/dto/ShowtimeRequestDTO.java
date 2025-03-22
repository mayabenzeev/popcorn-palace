package com.att.tdp.popcorn_palace.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class ShowtimeRequestDTO {

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    private Float price;
    @NotNull(message = "Movie ID is required")
    private Long movieId;
    @NotBlank(message = "Theater is required")
    private String theater;
    @NotNull(message = "Start time is required")
    private OffsetDateTime startTime;
    @NotNull(message = "End time is required")
    private OffsetDateTime endTime;

    public void setPrice(Float price) { this.price = price; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public void setTheater(String theater) { this.theater = theater; }
    public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }


    public Float getPrice() { return this.price;}
    public Long getMovieId() { return this.movieId; }
    public String getTheater() {return this.theater; }
    public OffsetDateTime getStartTime() { return this.startTime; }
    public OffsetDateTime getEndTime() {return this.endTime;}

}
