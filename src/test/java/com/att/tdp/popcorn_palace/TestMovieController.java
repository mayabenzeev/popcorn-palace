package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.controller.MoviesController;
import com.att.tdp.popcorn_palace.dto.MovieRequestDTO;

import com.att.tdp.popcorn_palace.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoviesController.class)
public class TestMovieController {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MovieService movieService;

    /** add movie tests */

    // add movie with null values test
    @Test
    @DisplayName("Should not add a movie with null values - 400")
    public void addMovieWithNullValues() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO(null, null, null, null, null);
        // mock a post request to /movies with a body of dto
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // add movie with empty values test
    @Test
    @DisplayName("Should not add a movie with empty values - 400")
    public void addMovieWithEmptyValues() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO("", "", 0, 0f, 0);
        // mock a post request to /movies with a body of dto
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // add movie with a massive duration time test
    @Test
    @DisplayName("Should not add a movie with a massive *invalid value* duration time - 400")
    public void addMovieWithMassiveDurationTime() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO("Avatar", "Action", 1000000, 8f, 2020);
        // mock a post request to /movies with a body of dto
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // add movie with invalid ratings values test
    @Test
    @DisplayName("Should not add a movie with negative ratings values - 400")
    public void addMovieWithNegativeRatingsValues() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO("Avatar", "Action", 120, -0.1f, 2020);
        // mock a post request to /movies with a body of dto
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // add movie with future release date test
    @Test
    @DisplayName("Should not add a movie with a future release date - 400")
    public void addMovieWithFutureReleaseDate() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO("Avatar", "Action", 120, 8f, 2030);
        // mock a post request to /movies with a body of dto
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // add movie with unexpected json keys test
    @Test
    @DisplayName("Should not add a movie with invalid json keys - 400")
    public void addMovieWithUnexpectedJsonKeys() throws Exception {
        String body = """
                {
                    "title":"Avatar",
                    "genre":"Action",
                    "duration":120,
                    "rating":8,
                    "releaseYear":2020,
                    "unexpectedKey":"unexpectedValue"
                }
            """;
        // mock a post request to /movies with a body of dto
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }


    /** update movie tests */

    // update movie with missing values test
    @Test
    @DisplayName("Should not update a movie with missing values - 400")
    public void updateMovieWithMissingValues() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO(null, null, 0, 0f, 0);
        // mock a put request to /movies/{title} with a body of dto
        mockMvc.perform(post("/movies/update/Avatar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // update movie with invalid rating test
    @Test
    @DisplayName("Should not update a movie with invalid rating - 400")
    public void updateMovieWithInvalidRating() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO("Avatar", "Action", 120, -0.1f, 2020);
        // mock a put request to /movies/{title} with a body of dto
        mockMvc.perform(post("/movies/update/Avatar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // update movie with no changes test
    @Test
    @DisplayName("Should update a movie with same title as in the endpoint- 200")
    public void updateMovieWithNoTitleChanges() throws Exception {

        MovieRequestDTO dto = new MovieRequestDTO("Avatar", "Action", 120, 8f, 2020);
        // mock a put request to /movies/{title} with a body of dto
        mockMvc.perform(post("/movies/update/Avatar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

    }


    /** delete movie tests */

    // delete movie database error test - tests how controller behaves when there is a service exception
    @Test
    @DisplayName("Should return 500 when database error occurs upon deletion - 500")
    public void deleteMovieDatabaseError() throws Exception {
        // mock a simulation to db failure that will throw an exception
        willThrow(new RuntimeException("DB failure")).given(movieService).deleteMovie("Avatar");

        // mock a delete request to /movies/{title}
        mockMvc.perform(delete("/movies/Avatar"))
                .andExpect(status().isInternalServerError());
    }
}
