package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Repository interface for managing Showtime entities.
 */
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    /**
     * Find overlapping showtimes in the same theater.
     * If a current showtime id is given (!= null), that search only among those that are not the current showtime.
     * @param currentId the current showtime id
     * @param theater the theater
     * @param startTime the start time
     * @param endTime the end time
     * @return the overlapping showtime if found
     */
    @Query("""
            SELECT st FROM Showtime st 
            WHERE st.theater = :theater
            AND (st.startTime < :endTime AND st.endTime > :startTime)
            AND (:currentId IS NULL OR st.id <> :currentId)""")
    Optional<Showtime> findOverlappingShowtimes(Long currentId,
                                                @Param("theater") String theater,
                                                @Param("startTime") OffsetDateTime startTime,
                                                @Param("endTime")OffsetDateTime endTime);
}
