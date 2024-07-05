package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Имя не задано. Вместо имени будет использоваться логин {}", user.getName());
        }

        String userEmail = user.getEmail();
        for (User userMap : users.values()) {
            if (userMap.getEmail().equals(userEmail)) {
                log.error("Попытка создания пользователя с уже занятым email = {}", user.getEmail());
                throw new ValidationException("Пользователь с таким email уже существует.");
            }
        }

        user.setId(getNextId());
        log.info("Для нового пользователя: {}, установлен ID {}", user.getName(), user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь с логином \"{}\" добавлен в коллекцию", user.getLogin());
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (users.containsKey(newUser.getId())) {
            User updatedUser = users.get(newUser.getId());
            updatedUser.setId(newUser.getId());
            updatedUser.setName(newUser.getName());
            updatedUser.setLogin(newUser.getLogin());
            updatedUser.setEmail(newUser.getEmail());
            updatedUser.setBirthday(newUser.getBirthday());
            users.put(updatedUser.getId(), updatedUser);
            log.info("В коллекцию сохранен обновленный пользователь с ID {}", newUser.getId());
            return updatedUser;
        } else {
            log.error("Пользователь с ID {} не найден в коллекции.", newUser.getId());
            throw new ValidationException("Пользователь для обновления не найден.");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}