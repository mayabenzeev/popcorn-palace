package com.att.tdp.popcorn_palace.entity;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "showtimes")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Float price;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie; // maintain the movie entity using the foreign key column movie_id
    @Column(nullable = false)
    private String theater;
    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startTime;
    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endTime;
    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings; // hold this field to be able to delete all bookings related to this showtime

    public void setId(Long id) { this.id = id;}
    public void setPrice(Float price) { this.price = price; }
    //    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public void setMovie(Movie movie) { this.movie = movie; }
    public void setTheater(String theater) { this.theater = theater; }
    public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }

    public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }

    public Long getId() { return this.id; }
    public Float getPrice() { return this.price;}
    public Movie getMovie() { return this.movie; }
    //    public Long getMovieId() { return this.movieId; }
    public String getTheater() {return this.theater; }
    public OffsetDateTime getStartTime() { return this.startTime; }

    public OffsetDateTime getEndTime() {return this.endTime;}

}
