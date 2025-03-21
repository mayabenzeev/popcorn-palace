package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
}
