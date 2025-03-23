package com.att.tdp.popcorn_palace.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

import java.time.Year;

/**
 * Data Transfer Object for movies requests.
 */
public class MovieRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Genre is required")
    private String genre;
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    @Max(value = 500, message = "Duration is limited 500 minutes")
    private Integer duration;
    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating value must be between 0 and 10")
    @Max(value = 10, message = "Rating value must be between 0 and 10")
    private Float rating;
    @NotNull(message = "Release year is required")
    @Min(value = 1000, message = "Release year must be greater than 1000")
    private Integer releaseYear;

    public MovieRequestDTO() {
    }

    public MovieRequestDTO(String title, String genre, Integer duration, Float rating, Integer releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    /**
     * Validates that the release year is not in the future.
     */
    @AssertTrue(message = "Release year cannot be in the future")
    @JsonIgnore
    public boolean isValidReleaseYear() {
        return this.releaseYear != null && this.releaseYear <= Year.now().getValue();
    }

    /** setters */
    public void setTitle(String newTitle) {this.title = newTitle;}
    public void setGenre(String newGenre) {this.genre = newGenre;}
    public void setDuration(Integer newDuration) { this.duration = newDuration;}
    public void setRating(Float newRating) { this.rating = newRating;}
    public void setReleaseYear(Integer newReleaseYear) { this.releaseYear = newReleaseYear;}

    /** getters */
    public String getTitle() {return this.title;}
    public String getGenre() {return this.genre;}
    public Integer getDuration() { return this.duration; }
    public Float getRating() { return this.rating; }
    public Integer getReleaseYear() {
        return this.releaseYear;
    }
}
