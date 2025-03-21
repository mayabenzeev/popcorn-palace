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
    Long id;
    @Column(nullable = false)
    Float price;
    // foreign key
    @Column(name = "movie_id", nullable = false)
    Long movieId;
    @Column(nullable = false)
    String theater;
    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    OffsetDateTime startTime;
    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    OffsetDateTime endTime;

}
