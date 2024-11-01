package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmRowMapper.class, GenreRepository.class, GenreRowMapper.class})
public class FilmRepositoryTest {
    private final FilmRepository filmRepository;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void createBeforeAll() {
        film1 = Film.builder()
                .name("Имя_Фильма_1")
                .description("Описание_1")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(150)
                .mpa(new Mpa(1, null))
                .genres(Set.of(new Genre(1, null)))
                .build();
        film2 = Film.builder()
                .name("Имя_Фильма_2")
                .description("Описание_2")
                .releaseDate(LocalDate.of(2015, 10, 10))
                .duration(100)
                .mpa(new Mpa(2, null))
                .genres(Set.of(new Genre(2, null)))
                .build();
    }

    @Test
    public void testFindAllFilm() {
        filmRepository.create(film1);
        filmRepository.create(film2);
        Assertions.assertThat(filmRepository.getAll()).hasSize(2);
    }

    @Test
    public void testFindFilmById() {
        filmRepository.create(film1);
        Assertions.assertThat(filmRepository.findFilmById(film1.getId())).hasFieldOrPropertyWithValue("name", "Имя_Фильма_1");
    }

    @Test
    public void testUpdateFilm() {
        filmRepository.create(film1);
        film1.setName("UPDATE");
        filmRepository.update(film1);
        Assertions.assertThat(filmRepository.findFilmById(1)).hasFieldOrPropertyWithValue("name", "UPDATE");
    }

    @Test
    public void testDeleteFilm() {
        filmRepository.create(film1);
        filmRepository.create(film2);
        filmRepository.delete(film2.getId());
        Assertions.assertThat(filmRepository.getAll()).hasSize(1);
    }
}