package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> getAll() {
        return inMemoryUserStorage.getAll();
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User findUserById(Long id) {
        return inMemoryUserStorage.findUserById(id);
    }

    public User update(User newUser) {
        return inMemoryUserStorage.update(newUser);
    }

    public void addUserToFriends(Long idUser, Long idFriend) {
        if (idUser != null && idFriend != null) {
            User user = inMemoryUserStorage.findUserById(idUser);
            User friend = inMemoryUserStorage.findUserById(idFriend);
            if (user.getFriends() == null) {
                user.setFriends(new HashSet<>());
                log.info("У пользователя с id {} был создан новый список для хранения id друзей", idUser);
            }
            user.getFriends().add(idFriend);
            log.info("Пользователь с id {} добавлен в друзья к пользователю с id {}", idFriend, idUser);
            if (friend.getFriends() == null) {
                friend.setFriends(new HashSet<>());
                log.info("У пользователя с id {} был создан новый список для хранения id друзей", idFriend);
            }
            friend.getFriends().add(idUser);
            log.info("Пользователь с id {} добавлен в друзья к пользователю с id {}", idUser, idFriend);
        } else {
            log.error("ID пользователей некорректны. ID: idUser {}, idFriend {}", idUser, idFriend);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public void deleteUserFromFriends(Long idUser, Long idFriend) {
        if (idUser != null && idFriend != null) {
            User user = findUserById(idUser);
            User friend = findUserById(idFriend);
                user.getFriends().remove(idFriend);
                log.info("Удалили друга у пользователя с id {}. ID удаленного друга: {}", idUser, idFriend);
                friend.getFriends().remove(idUser);
                log.info("Удалили друга у пользователя с id {}. ID удаленного друга: {}", idFriend, idUser);
        } else {
            log.error("ID пользователей некорректны. ID: idUser {}, idFriend {}", idUser, idFriend);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public Collection<User> showCommonFriends(Long idUser, Long idFriend) {
        if (idUser != null && idFriend != null) {
            User user = findUserById(idUser);
            User friend = findUserById(idFriend);
            if (user.getFriends().contains(friend.getId()) && friend.getFriends().contains(user.getId())) {
                Set<Long> idsFriend = friend.getFriends();
                return user.getFriends().stream()
                        .filter(idsFriend::contains)
                        .map(inMemoryUserStorage::findUserById)
                        .toList();
            } else {
                log.error("Пользователи не являются друзьями. ID: idUser {}, idFriend {}", idUser, idFriend);
                throw new ValidationException("Пользователи не являются друзьями.");
            }

        } else {
            log.error("ID пользователей некорректны. ID: idUser {}, idFriend {}", idUser, idFriend);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public Collection<User> getFriends(Long id) {
        if (id != null) {
            User user = findUserById(id);
            return user.getFriends().stream()
                    .map(inMemoryUserStorage::findUserById)
                    .toList();
        } else {
            log.error("ID не задан.");
            throw new ValidationException("ID пользователя не задан или некорректен.");
        }
    }
}