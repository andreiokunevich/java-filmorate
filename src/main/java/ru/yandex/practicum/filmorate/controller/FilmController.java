package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private long idCounter = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза задана раньше чем 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза фильма задана неверно.");
        }

        film.setId(getNextId());
        log.info("Для нового фильма: {}, установлен ID {}", film.getName(), film.getId());
        films.put(film.getId(), film);
        log.info("Фильм с названием \"{}\" добавлен в коллекцию", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film updatedFilm = films.get(newFilm.getId());
            updatedFilm.setId(newFilm.getId());
            updatedFilm.setName(newFilm.getName());
            updatedFilm.setDescription(newFilm.getDescription());

            if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("При обновлении фильма дата релиза задана раньше чем 28 декабря 1895 года.");
                throw new ValidationException("Дата релиза фильма задана неверно.");
            } else {
                updatedFilm.setReleaseDate(newFilm.getReleaseDate());
            }

            updatedFilm.setDuration(newFilm.getDuration());
            films.put(updatedFilm.getId(), updatedFilm);
            log.info("В коллекцию сохранен обновленный фильм с ID {}", updatedFilm.getId());
            return updatedFilm;
        } else {
            log.error("Фильм с ID {} не найден в коллекции.", newFilm.getId());
            throw new ValidationException("Фильм с заданным ID не найден.");
        }
    }

    private long getNextId() {
        return idCounter++;
    }
}