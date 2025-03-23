package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.controller.MoviesController;
import com.att.tdp.popcorn_palace.controller.ShowtimesController;
import com.att.tdp.popcorn_palace.dto.ShowtimeRequestDTO;
import com.att.tdp.popcorn_palace.exception.NotFoundException;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowtimesController.class)
public class TestShowtimeController {
    @Autowired
    private MockMvc mockMvc; // inject mock HTTP testing client
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShowtimeService showtimeService;

    /**
     * add showtime tests
     */

    // add showtime with null values test
    @Test
    @DisplayName("Should not add a showtime with null values - 400")
    public void addShowtimeWithNullValues() throws Exception {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(null, null, null, null, null);
        // mock a post request to /showtimes with a body of dto
        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // add showtime with empty values test
    @Test
    @DisplayName("Should not add a showtime with empty values - 400")
    public void addShowtimeWithEmptyValues() throws Exception {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(0f, 0L, "", null, null);
        // mock a post request to /showtimes with a body of dto
        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // add showtime that has already started test
    @Test
    @DisplayName("Should not add a showtime that has already started - 400")
    public void addShowtimeThatHasAlreadyStarted() throws Exception {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(15f, 1L, "Theater 1",
                OffsetDateTime.now().minusHours(1), OffsetDateTime.now().plusHours(1));
        // mock a post request to /showtimes with a body of dto
        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    /**
     * update showtime tests
     */
    // update showtime with empty values test
    @Test
    @DisplayName("Should not update a showtime with empty values - 400")
    public void updateShowtimeWithEmptyValues() throws Exception {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(0f, 0L, "",
                OffsetDateTime.now().plusHours(1), OffsetDateTime.now().plusHours(2));
        // mock a post request to showtimes/update/1 with a body of dto
        mockMvc.perform(post("/showtimes/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // update showtime with a negative price test
    @Test
    @DisplayName("Should not update a showtime with a negative price - 400")
    public void updateShowtimeWithNegativePrice() throws Exception {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(-15f, 1L, "Theater 1",
                OffsetDateTime.now().plusHours(1), OffsetDateTime.now().plusHours(2));
        // mock a post request to /showtimes/update/1 with a body of dto
        mockMvc.perform(post("/showtimes/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


    /** delete showtime tests */
    // delete showtime with a non-existing id test
    @Test
    @DisplayName("Should throw NotFoundException when deleting a showtime with a non-existing id - 404")
    public void deleteShowtimeWithNonExistingId() throws Exception {
        // mock a delete request to /showtimes/1
        willThrow(new NotFoundException("Showtime with id 1 not found"))
                .given(showtimeService).deleteShowtime(1L);
        mockMvc.perform(delete("/showtimes/1"))
                .andExpect(status().isNotFound());
    }
}
