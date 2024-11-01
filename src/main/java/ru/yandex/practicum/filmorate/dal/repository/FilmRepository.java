package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Set;

@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private final GenreRepository genreRepository;

    private static final String ALL_FILMS_QUERY = "SELECT * FROM film AS f LEFT JOIN rating_MPA AS r ON " +
            "f.film_rating_MPA_id = r.rating_mpa_id";
    private static final String FILM_BY_ID_QUERY = "SELECT * FROM film AS f INNER JOIN rating_MPA AS r ON " +
            "f.film_rating_MPA_id = r.rating_mpa_id  AND film_id = ?";
    private static final String ADD_FILM_QUERY = "INSERT INTO film " +
            "(film_name, film_description, film_releaseDate, film_duration, film_rating_MPA_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE film SET film_name = ?, film_description = ?, " +
            "film_releaseDate = ?, film_duration = ?, film_rating_MPA_id = ? WHERE film_id = ?";
    private static final String DELETE_FILM_QUERY = "DELETE FROM film WHERE film_id = ?";
    private static final String TOP_POPULAR_FILMS_QUERY = "SELECT * FROM film AS f LEFT JOIN rating_MPA AS r " +
            "ON f.film_rating_MPA_id = r.rating_mpa_id LEFT JOIN (SELECT film_id, COUNT(film_id) AS likes FROM film_like " +
            "GROUP BY film_id) fl ON f.film_id = fl.film_id ORDER BY likes DESC LIMIT ?";
    private static final String ADD_LIKE_FILM_QUERY = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE_QUERY = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, GenreRepository genreRepository) {
        super(jdbc, mapper);
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = findMany(ALL_FILMS_QUERY);
        for (Film film : films) {
            Set<Genre> genres = genreRepository.getGenresByFilmId(film.getId());
            if (!genres.isEmpty()) {
                film.setGenres(genres);
            }
        }
        return films;
    }

    @Override
    public Film findFilmById(Integer id) {
        Film film = findOne(FILM_BY_ID_QUERY, id);
        film.setGenres(genreRepository.getGenresByFilmId(id));
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer amount) {
        Collection<Film> films = findMany(TOP_POPULAR_FILMS_QUERY, amount);
        for (Film film : films) {
            Set<Genre> genres = genreRepository.getGenresByFilmId(film.getId());
            if (!genres.isEmpty()) {
                film.setGenres(genres);
            }
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        Integer id = insert(
                ADD_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        update(
                UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public void delete(Integer id) {
        delete(DELETE_FILM_QUERY, id);
    }

    public void addLike(Integer idFilm, Integer idUser) {
        insertKeys(ADD_LIKE_FILM_QUERY, idFilm, idUser);
    }

    public void deleteLike(Integer idFilm, Integer idUser) {
        update(REMOVE_LIKE_QUERY, idFilm, idUser);
    }
}