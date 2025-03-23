package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.BookingRequestDTO;
import com.att.tdp.popcorn_palace.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingsController {

    private final BookingService bookingService;

    @Autowired
    public BookingsController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> bookTicket(@RequestBody @Valid BookingRequestDTO bookingDTO) {
        return ResponseEntity.ok(Map.of("bookingId", bookingService.bookTicket(bookingDTO).getId()));
    }
}
