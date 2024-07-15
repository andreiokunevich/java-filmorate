package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void createFilmController() {
        filmController = new FilmController();
    }

    @Test
    void shouldBeZeroFilmsInEmptyList() {
        Assertions.assertEquals(0, filmController.getAllFilms().size(), "Коллекция не пустая.");
    }

    @Test
    void shouldThrowExceptionWhenWrongDate() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1795, 12, 28));

        assertThrows(ValidationException.class, () -> filmController.createFilm(film), "Фильм с неверной датой добавлен в коллекцию.");
    }

    @Test
    void shouldNotThrowExceptionWithCorrectDate() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(2000, 12, 28));

        assertDoesNotThrow(() -> filmController.createFilm(film), "При верно заданной дате было выброшено исключение.");
    }

    @Test
    void shouldThrowExceptionWhenPassWrongIDInUpdate() {
        Film film = new Film();
        Film film1 = new Film();
        film1.setId(10L);

        assertThrows(ValidationException.class, () -> filmController.updateFilm(film1), "Произошло обновление фильма с неверным айди.");
    }

    @Test
    void shouldThrowExceptionWhileUpdateWrongRelease() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1785, 12, 28));

        assertThrows(ValidationException.class, () -> filmController.updateFilm(film),"Произошло обновление даты выхода фильма на недопустимую.");
    }
}