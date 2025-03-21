package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
