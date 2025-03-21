package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"title", "genre", "duration", "releaseYear"})}) //to prevent inserting two movies with the same values

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
}
