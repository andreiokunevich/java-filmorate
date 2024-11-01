package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film findFilmById(Integer id);

    Collection<Film> getPopularFilms(Integer amount);

    Film create(Film film);

    Film update(Film film);

    void delete(Integer id);

    void addLike(Integer idFilm, Integer idUser);

    void deleteLike(Integer idFilm, Integer idUser);
}