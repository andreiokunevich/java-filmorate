package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultset, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultset.getInt("film_id"));
        film.setName(resultset.getString("film_name"));
        film.setDescription(resultset.getString("film_description"));
        film.setReleaseDate(resultset.getDate("film_releaseDate").toLocalDate());
        film.setDuration(resultset.getInt("film_duration"));
        film.setMpa(new Mpa(resultset.getInt("rating_mpa_id"), resultset.getString("rating_mpa_name")));
        return film;
    }
}