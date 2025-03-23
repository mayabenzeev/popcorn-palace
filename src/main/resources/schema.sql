DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS showtimes;
DROP TABLE IF EXISTS movies;

CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    genre VARCHAR(255) NOT NULL,
    duration INTEGER NOT NULL,
    rating FLOAT NOT NULL,
    release_year INTEGER NOT NULL
);

CREATE TABLE showtimes (
    id SERIAL PRIMARY KEY,
    price FLOAT NOT NULL,
    theater VARCHAR(255) NOT NULL,
    start_time TIMESTAMPZ NOT NULL,
    end_time TIMESTAMPZ NOT NULL,
    movie_id INTEGER NOT NULL,
    CONSTRAINT fk_showtime_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    seat_number INTEGER NOT NULL,
    showtime_id INTEGER NOT NULL,
    CONSTRAINT fk_booking_showtime FOREIGN KEY (showtime_id) REFERENCES showtimes(id) ON DELETE CASCADE,
    CONSTRAINT uk_booking_unique_seat UNIQUE (showtime_id, seat_number)
);
