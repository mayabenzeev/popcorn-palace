package com.att.tdp.popcorn_palace.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ShowTime {
    Long id;
    Float price;
    Movie movie;
    String theater;
    OffsetDateTime startTime;
    OffsetDateTime endTime;


    /**
     * Instantiates a new ShowTime.
     */
    public ShowTime(){}

    public ShowTime(float price, Movie movie, String theater, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.price = price;
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
