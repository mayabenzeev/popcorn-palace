package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.controller.BookingsController;
import com.att.tdp.popcorn_palace.dto.BookingRequestDTO;
import com.att.tdp.popcorn_palace.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingsController.class)
public class TestBookingController {
    @Autowired
    private MockMvc mockMvc; // inject mock HTTP testing client
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;

    /** add booking tests */

    // add booking with null values test
    @Test
    @DisplayName("Should not add a booking with null values - 400")
    public void addBookingWithNullValues() throws Exception {
        BookingRequestDTO dto = new BookingRequestDTO(UUID.randomUUID(), null, null);
        // mock a post request to /bookings with a body of null
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    // add booking with empty values test
    @Test
    @DisplayName("Should not add a booking with empty values - 400")
    public void addBookingWithEmptyValues() throws Exception {
        BookingRequestDTO dto = new BookingRequestDTO(UUID.randomUUID(), 0L, 0);
        // mock a post request to /bookings with a body of empty values
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // add booking with a too big seat number test
    @Test
    @DisplayName("Should not add a booking with a too big seat number - 400")
    public void addBookingWithTooBigSeatNumber() throws Exception {
        BookingRequestDTO dto = new BookingRequestDTO(UUID.randomUUID(), 1L, 10000);
        // mock a post request to /bookings with a body of a too big seat number
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
