package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void createUserController() {
        userController = new UserController();
    }

    @Test
    void shouldBeZeroUserInList() {
        Assertions.assertEquals(0, userController.getAllUsers().size(), "Коллекция не пустая.");
    }

    @Test
    void shouldThrowExceptionWithDuplicatedEmail() {
        User user = new User();
        user.setEmail("pochta@mail.ru");
        userController.createUser(user);
        System.out.println(user);
        User user2 = new User();
        user2.setEmail("pochta@mail.ru");


        assertThrows(ValidationException.class, () -> userController.createUser(user2), "Получилось добавить пользователя с уже существующей почтой.");
    }

    @Test
    void shouldThrowExceptionWhenPassWrongIDInUpdate() {
        User user = new User();
        User user1 = new User();
        user1.setId(10L);

        assertThrows(ValidationException.class, () -> userController.updateUser(user1), "Получилось обновить данные пользователя с неверным айди.");
    }
}