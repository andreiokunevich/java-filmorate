package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dal.repository.MpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("filmRepository")
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film findFilmById(Integer id) {
        return filmStorage.findFilmById(id);
    }

    public Film create(Film film) {
        Film createdFilm = filmStorage.create(film);

        if (!createdFilm.getGenres().isEmpty()) {
            List<Integer> genresIds = genreRepository.getAllGenres()
                    .stream()
                    .map(Genre::getId)
                    .toList();
            List<Integer> genresInFilmIds = film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .toList();
            if (!genresIds.containsAll(genresInFilmIds)) {
                throw new ValidationException("Передан неверный жанр");
            }
            for (Integer genreId : genresIds) {
                if (genresInFilmIds.contains(genreId)) {
                    genreRepository.addFilmGenre(createdFilm.getId(), genreId);
                }
            }
        }

        List<Integer> mpaId = mpaRepository.getAllMpa()
                .stream()
                .map(Mpa::getId)
                .toList();

        if (film.getMpa() != null) {
            if (!mpaId.contains(film.getMpa().getId())) {
                throw new ValidationException("Рейтинг MPA не верен");
            }
        }
        return createdFilm;
    }

    public Film update(Film film) {
        if (filmStorage.findFilmById(film.getId()) == null) {
            throw new NotFoundException("Айди фильма не найден");
        }
        Film updatedFilm = filmStorage.update(film);
        if (!updatedFilm.getGenres().isEmpty()) {
            genreRepository.deleteFilmGenre(film.getId());
            List<Integer> genresIds = genreRepository.getAllGenres()
                    .stream()
                    .map(Genre::getId)
                    .toList();
            List<Integer> genresInFilmIds = film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .toList();
            for (Integer genreId : genresIds) {
                if (genresInFilmIds.contains(genreId)) {
                    genreRepository.addFilmGenre(updatedFilm.getId(), genreId);
                }
            }
        }
        return updatedFilm;
    }

    public void delete(Integer id) {
        filmStorage.delete(id);
    }

    public void addLike(Integer idFilm, Integer idUser) {
        if (idFilm != null && idUser != null) {
            filmStorage.findFilmById(idFilm);
            filmStorage.addLike(idFilm, idUser);
            log.info("Добавили лайк фильму с id {} от пользователя с id {}", idFilm, idUser);
        } else {
            log.error("ID некорректны. ID: idUser {}, idFilm {}", idUser, idFilm);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public void deleteLike(Integer idFilm, Integer idUser) {
        if (idFilm != null && idUser != null) {
            filmStorage.findFilmById(idFilm);
            filmStorage.deleteLike(idFilm, idUser);
            log.info("Удалили лайк у фильма с id {} от пользователя с id {}", idFilm, idUser);
        } else {
            log.error("ID некорректны. ID: idUser {}, idFilm {}", idUser, idFilm);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public Collection<Film> getPopularFilms(Integer amount) {
        if (amount != null) {
            return filmStorage.getPopularFilms(amount);
        } else {
            log.error("Параметр amount некорректен");
            throw new ValidationException("Параметр amount некорректен.");
        }
    }
}