package com.att.tdp.popcorn_palace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * Data Transfer Object for showtime requests.
 */
@JsonIgnoreProperties(ignoreUnknown = false)

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

    public ShowtimeRequestDTO() {
    }

    public ShowtimeRequestDTO(Float price, Long movieId, String theater, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.price = price;
        this.movieId = movieId;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Validates that the start time is not in the past.
     */
    @AssertTrue(message = "Start time cannot be in the past")
    @JsonIgnore
    public boolean isValidStartTime() {
        return this.startTime != null && this.startTime.isAfter(OffsetDateTime.now());
    }

    /**
     * Validates that the end time is not in the past.
     */
    @AssertTrue(message = "End time cannot be in the past")
    @JsonIgnore
    public boolean isValidEndTime() {
        return this.endTime != null && this.endTime.isAfter(OffsetDateTime.now());
    }

    /** setters */
    public void setPrice(Float price) { this.price = price; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public void setTheater(String theater) { this.theater = theater; }
    public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }

    /** getters */
    public Float getPrice() { return this.price;}
    public Long getMovieId() { return this.movieId; }
    public String getTheater() {return this.theater; }
    public OffsetDateTime getStartTime() { return this.startTime; }
    public OffsetDateTime getEndTime() {return this.endTime;}

}
