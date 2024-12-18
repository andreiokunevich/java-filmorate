package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User create(User user);

    User findUserById(Integer id);

    User update(User newUser);

    void delete(Integer id);
}