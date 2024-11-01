package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class})
public class UserRepositoryTest {
    private final UserRepository userRepository;

    @Test
    public void testGetAllUsers() {
        Assertions.assertThat(userRepository.getAllUsers()).hasSize(5);
    }

    @Test
    public void testFindUserById() {
        Assertions.assertThat(userRepository.findUserById(1)).hasFieldOrPropertyWithValue("name", "Имя_1");
    }

    @Test
    public void testUpdateUser() {
        User user = userRepository.findUserById(1);
        user.setName("UPDATE");
        userRepository.update(user);
        Assertions.assertThat(userRepository.findUserById(1)).hasFieldOrPropertyWithValue("name", "UPDATE");
    }

    @Test
    public void testDeleteUser() {
        userRepository.delete(1);
        Assertions.assertThat(userRepository.getAllUsers()).hasSize(4);
    }
}