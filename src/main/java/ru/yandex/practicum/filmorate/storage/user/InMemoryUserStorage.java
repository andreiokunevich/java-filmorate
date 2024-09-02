package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private long idCounter = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
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

    @Override
    public User findUserById(Long id) {
        log.info("Попытка получения пользователя по id {}", id);
        if(users.get(id)!=null){
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }
    }

    @Override
    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User updatedUser = users.get(newUser.getId());
            updatedUser.setId(newUser.getId());

            if (newUser.getName() == null) {
                updatedUser.setName(newUser.getLogin());
                log.info("При обновлении пользователя имя не задано. Вместо имени будет использоваться логин {}", updatedUser.getName());
            } else {
                updatedUser.setName(newUser.getName());
            }

            updatedUser.setLogin(newUser.getLogin());
            updatedUser.setEmail(newUser.getEmail());
            updatedUser.setBirthday(newUser.getBirthday());
            users.put(updatedUser.getId(), updatedUser);
            log.info("В коллекцию сохранен обновленный пользователь с ID {}", newUser.getId());
            return updatedUser;
        } else {
            log.error("Пользователь с ID {} не найден в коллекции.", newUser.getId());
            throw new NotFoundException("Пользователь для обновления не найден.");
        }
    }

    private long getNextId() {
        return idCounter++;
    }
}