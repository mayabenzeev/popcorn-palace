package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequestDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.att.tdp.popcorn_palace.service.ShowtimeService;

/**
 * REST controller for managing showtimes.
 */
@RestController
@RequestMapping("/showtimes")
public class ShowtimesController {
    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimesController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponseDTO> getShowtimeById(@PathVariable Long id) {
        return ResponseEntity.ok(new ShowtimeResponseDTO(showtimeService.getShowtimeById(id)));
    }

    @PostMapping
    public ResponseEntity<ShowtimeResponseDTO> addShowtime(@RequestBody @Valid ShowtimeRequestDTO showtimeDTO) {
        return ResponseEntity.ok(new ShowtimeResponseDTO(showtimeService.addShowtime(showtimeDTO)));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Void> updateShowtime(
            @PathVariable Long id, @RequestBody @Valid ShowtimeRequestDTO updatedShowtimeDTO){
        showtimeService.updateShowtime(id, updatedShowtimeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.ok().build();    }
}
