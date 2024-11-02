package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String ALL_MPA_QUERY = "SELECT * FROM rating_MPA";
    private static final String MPA_BY_ID_QUERY = "SELECT * FROM rating_MPA WHERE rating_mpa_id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Mpa> getAllMpa() {
        return findMany(ALL_MPA_QUERY);
    }

    public Mpa getMpaById(Long id) {
        return findOne(MPA_BY_ID_QUERY, id);
    }
}