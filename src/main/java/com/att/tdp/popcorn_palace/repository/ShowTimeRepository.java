package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    // find overlapping show times in the same theater
    @Query("""
            SELECT st FROM ShowTime st 
            WHERE s.theater = :theater
            AND (s.startTime < :endTime AND s.endTime > :startTime)""")
    Optional<ShowTime> findOverlappingShowTimes(@Param("theater") String theater,
                                                @Param("startTime") OffsetDateTime startTime,
                                                @Param("endTime")OffsetDateTime endTime);
}
