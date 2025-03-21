package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.UUID;

/**
 * The type Movie.
 */
public class Movie {
    Long id; //JPA auto generated - leave it null
    String title;
    String genre;
    Integer duration;
    Float rating;
    Integer releaseYear;


    /**
     * Instantiates a new Movie.
     */
    public Movie(){}

    public Movie(String title, String genre, int duration, float rating, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }


}
