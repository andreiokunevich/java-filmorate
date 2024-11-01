package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String ALL_GENRES_QUERY = "SELECT * FROM genre";
    private static final String GENRE_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String ADD_FILM_GENRE_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRE_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String GENRES_FOR_FILM_QUERY = "SELECT g.genre_id,g.genre_name " +
            "FROM genre AS g JOIN film_genres AS fg ON g.genre_id = fg.genre_id WHERE fg.film_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getAllGenres() {
        return findMany(ALL_GENRES_QUERY);
    }

    public Genre getGenreById(Integer id) {
        return findOne(GENRE_BY_ID_QUERY, id);
    }

    public void addFilmGenre(Integer filmId, Integer genreId) {
        insertKeys(ADD_FILM_GENRE_QUERY, filmId, genreId);
    }

    public boolean deleteFilmGenre(Integer id) {
        return delete(DELETE_FILM_GENRE_QUERY, id);
    }

    public Set<Genre> getGenresByFilmId(Integer id) {
        return new HashSet<>(findMany(GENRES_FOR_FILM_QUERY, id));
    }
}