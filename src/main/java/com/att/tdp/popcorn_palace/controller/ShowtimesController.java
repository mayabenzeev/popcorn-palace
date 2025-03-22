package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.entity.Showtime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.att.tdp.popcorn_palace.service.ShowtimeService;

@RestController
@RequestMapping("/showtimes")
public class ShowtimesController {
    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimesController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping("/{id}")
    public Showtime getShowtimeById(@PathVariable Long id) {
        return showtimeService.getShowtimeById(id);
    }

    @PostMapping
    public Showtime addShowtime(@RequestBody Showtime showtime) {
        return showtimeService.addShowtime(showtime);
    }

    @PostMapping("/update/{id}")
    public void updateShowtime(@PathVariable Long id, @RequestBody Showtime updatedShowtime) {
        showtimeService.updateShowtime(id, updatedShowtime);
    }

    @DeleteMapping("/{id}")
    public void deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
    }

}
