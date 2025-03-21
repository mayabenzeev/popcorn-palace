package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"title", "genre", "duration", "release_year"})}) //to prevent inserting two movies with the same values

public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //JPA auto generated - leave it null
    @Column(nullable = false) // title cannot be null
    private String title;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private Integer duration;
    @Column(nullable = false)
    private Float rating;
    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setGenre(String newGenre) {
        this.genre = newGenre;
    }
    public void setDuration(Integer newDuration) {
        this.duration = newDuration;
    }
    public void setRating(Float newRating) {
        this.rating = newRating;
    }
    public void setReleaseYear(Integer newReleaseYear) {
        this.releaseYear = newReleaseYear;
    }
}
