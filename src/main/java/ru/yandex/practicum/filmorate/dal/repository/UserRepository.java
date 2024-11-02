package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {

    private static final String ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String ADD_USER_QUERY = "INSERT INTO users (user_name, user_email, user_login, user_birthday) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET user_name = ?, user_email = ? , user_login = ?, user_birthday = ? WHERE user_id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(ALL_USERS_QUERY);
    }

    @Override
    public User findUserById(Integer id) {
        return findOne(USER_BY_ID_QUERY, id);
    }

    @Override
    public User create(User user) {
        Integer id = insert(
                ADD_USER_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_USER_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public void delete(Integer id) {
        delete(DELETE_USER_QUERY, id);
    }
}