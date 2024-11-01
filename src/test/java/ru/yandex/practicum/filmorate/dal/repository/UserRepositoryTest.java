package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class})
public class UserRepositoryTest {
    private final UserRepository userRepository;
    private User user1;
    private User user2;

    @BeforeEach
    public void createBeforeAll() {
        user1 = User.builder()
                .name("Имя_1")
                .email("mail1@mail.ru")
                .login("login_1")
                .birthday(LocalDate.of(1991, 10, 10))
                .build();
        user2 = User.builder()
                .name("Имя_2")
                .email("mail2@mail.ru")
                .login("login_2")
                .birthday(LocalDate.of(2015, 10, 10))
                .build();
    }

    @Test
    public void testGetAllUsers() {
        userRepository.create(user1);
        userRepository.create(user2);
        Assertions.assertThat(userRepository.getAllUsers()).hasSize(2);
    }

    @Test
    public void testFindUserById() {
        userRepository.create(user1);
        Assertions.assertThat(userRepository.findUserById(user1.getId())).hasFieldOrPropertyWithValue("name", "Имя_1");
    }

    @Test
    public void testUpdateUser() {
        userRepository.create(user1);
        user1.setName("UPDATE");
        userRepository.update(user1);
        Assertions.assertThat(userRepository.findUserById(user1.getId())).hasFieldOrPropertyWithValue("name", "UPDATE");
    }

    @Test
    public void testDeleteUser() {
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.delete(user2.getId());
        Assertions.assertThat(userRepository.getAllUsers()).hasSize(1);
    }
}