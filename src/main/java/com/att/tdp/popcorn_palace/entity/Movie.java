package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"title"})}) //to prevent inserting two movies with the same title (the defined rule)

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
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes; // hold this field to be able to delete all showtimes related to this movie

    public void setTitle(String newTitle) {this.title = newTitle;}
    public void setGenre(String newGenre) {this.genre = newGenre;}
    public void setDuration(Integer newDuration) { this.duration = newDuration;}
    public void setRating(Float newRating) { this.rating = newRating;}
    public void setReleaseYear(Integer newReleaseYear) { this.releaseYear = newReleaseYear;}

    public Long getId() { return this.id;}
    public String getTitle() {return this.title;}
    public String getGenre() {return this.genre;}
    public Integer getDuration() { return this.duration; }
    public Float getRating() { return this.rating; }
    public Integer getReleaseYear() {return this.releaseYear;}


}
