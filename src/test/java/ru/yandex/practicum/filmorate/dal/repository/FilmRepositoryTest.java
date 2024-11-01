package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmRowMapper.class, GenreRepository.class, GenreRowMapper.class})
public class FilmRepositoryTest {
    private final FilmRepository filmRepository;

    @Test
    public void testFindAllFilm() {
        Assertions.assertThat(filmRepository.getAll()).hasSize(5);
    }

    @Test
    public void testFindFilmById() {
        Assertions.assertThat(filmRepository.findFilmById(1)).hasFieldOrPropertyWithValue("name", "Имя_Фильма_1");
    }

    @Test
    public void testUpdateFilm() {
        Film film = filmRepository.findFilmById(1);
        film.setName("UPDATE");
        filmRepository.update(film);
        Assertions.assertThat(filmRepository.findFilmById(1)).hasFieldOrPropertyWithValue("name", "UPDATE");
    }

    @Test
    public void testDeleteFilm() {
        filmRepository.delete(1);
        Assertions.assertThat(filmRepository.getAll()).hasSize(4);
    }
}