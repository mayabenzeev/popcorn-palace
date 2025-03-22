package com.att.tdp.popcorn_palace.entity;

import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "showtimes")
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Float price;
    // foreign key
    @Column(name = "movie_id", nullable = false)
    private Long movieId;
    @Column(nullable = false)
    private String theater;
    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startTime;
    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endTime;

    public Long getId() { return this.id; }
    public Long getMovieId() { return this.movieId; }
    public String getTheater() {
        return this.theater;
    }
    public OffsetDateTime getStartTime() {
        return this.startTime;
    }
    public OffsetDateTime getEndTime() {
        return this.endTime;
    }

    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public void setTheater(String theater) { this.theater = theater; }
    public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }
}
