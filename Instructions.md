# Popcorn Palace – Cinema Booking Service

## Table of Contents
* [General Info](#general-info)
* [Setup & Run Instructions](#setup--run-instructions)
* [Authors](#authors)

---

## General Info
This project is part of the AT&T Tech Development Program Assignment. 
It is a backend microservice for managing cinema-related operations, including movies, showtimes, and bookings.

Technologies used:
- Java 21
- Spring Boot
- PostgreSQL
- Docker + Docker Compose
- Maven


The application supports:
- Managing movies (CRUD)
- Scheduling showtimes per movie
- Booking seats per showtime
- Validation, exception handling, and cascading deletions (deleting a movie deletes it's showtimes and the related bookings)

---

## Setup & Run Instructions

### Build the Application

1. Clone the repo:
```bash
git clone https://github.com/mayabenzeev/popcorn-palace.git
cd popcorn-palace
```
2. Build the application:
```bash
docker compose up --build
```
3. Access the application at `http://localhost:8080/movies/all` for example, via web browser or through postman.
4. You can connect to PostgreSQL at `localhost:5432` with the credentials:
```bash
Host:     localhost
Port:     5432
Username: popcorn-palace
Password: popcorn-palace
Database: popcorn-palace
```
Via terminal with psql CLI: `psql -h localhost -p 5432 -U popcorn-palace -d popcorn-palace` prompted for the password: popcorn-palace
Or via inside app container: `docker exec -it popcorn-palace-db psql -U popcorn-palace -d popcorn-palace` 
(get out with `\q`)
5. To stop the application:
```bash
docker compose down
```

## Docker Architecture

- **app**: Spring Boot microservice, exposing RESTful APIs on port 8080
- **db**: PostgreSQL container
- **network**: Internal Docker network for secure communication

---

## Authors

Developed by:  
**Maya Ben-Zeev**
