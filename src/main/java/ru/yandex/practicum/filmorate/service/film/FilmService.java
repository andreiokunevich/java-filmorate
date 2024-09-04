package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserService userService;
    private final FilmStorage inMemoryFilmStorage;

    public Collection<Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    public Film findFilmById(Long id) {
        return inMemoryFilmStorage.findFilmById(id);
    }

    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    public Film update(Film newFilm) {
        return inMemoryFilmStorage.update(newFilm);
    }

    public void addLike(Long idFilm, Long idUser) {
        if (idFilm != null && idUser != null) {
            Film film = findFilmById(idFilm);
            userService.findUserById(idUser);
            film.getLikes().add(idUser);
            log.info("Добавили лайк фильму с id {} от пользователя с id {}", idFilm, idUser);
        } else {
            log.error("ID некорректны. ID: idUser {}, idFilm {}", idUser, idFilm);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public void deleteLike(Long idFilm, Long idUser) {
        if (idFilm != null && idUser != null) {
            Film film = findFilmById(idFilm);
            userService.findUserById(idUser);
            film.getLikes().remove(idUser);
        } else {
            log.error("ID некорректны. ID: idUser {}, idFilm {}", idUser, idFilm);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public Collection<Film> getPopularFilms(Long count) {
        if (count != null) {
            return getAll().stream()
                    .sorted(Comparator.comparingInt((Film film) -> Optional.ofNullable(film.getLikes()).map(Set::size)
                            .orElse(0)).reversed())
                    .limit(count)
                    .toList();
        } else {
            log.error("Параметр count некорректен");
            throw new ValidationException("Параметр count некорректен.");
        }
    }
}