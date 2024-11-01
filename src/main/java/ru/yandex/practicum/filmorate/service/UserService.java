package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("userRepository")
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipRepository friendshipRepository;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User findUserById(Integer id) {
        return userStorage.findUserById(id);
    }

    public User update(User user) {
        userStorage.findUserById(user.getId());
        return userStorage.update(user);
    }

    public void delete(Integer id) {
        userStorage.delete(id);
    }

    public void addUserToFriends(Integer idUser, Integer idFriend) {
        if (idUser != null && idFriend != null) {
            userStorage.findUserById(idUser);
            userStorage.findUserById(idFriend);
            friendshipRepository.addFriend(idUser, idFriend);
            log.info("Пользователь с id {} добавлен в друзья к пользователю с id {}", idFriend, idUser);
        } else {
            log.error("ID пользователей некорректны или не найдены. ID: idUser {}, idFriend {}", idUser, idFriend);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public void deleteUserFromFriends(Integer idUser, Integer idFriend) {
        if (idUser != null && idFriend != null) {
            userStorage.findUserById(idUser);
            Collection<User> friendList = getFriends(idUser);
            if (friendList.contains(userStorage.findUserById(idFriend))) {
                friendshipRepository.deleteFriend(idUser, idFriend);
                log.info("Удалили друга у пользователя с id {}. ID удаленного друга: {}", idUser, idFriend);
            }
        } else {
            log.error("ID пользователей некорректны или не найдены. ID: idUser {}, idFriend {}", idUser, idFriend);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public Collection<User> showCommonFriends(Integer idUser, Integer idFriend) {
        if (idUser != null && idFriend != null) {
            userStorage.findUserById(idUser);
            userStorage.findUserById(idFriend);
            Collection<User> commonFriends = friendshipRepository.getCommonFriends(idUser, idFriend);
            if (!commonFriends.isEmpty()) {
                return commonFriends;
            } else {
                log.error("Пользователи не являются друзьями или у них нет общих друзей. ID: idUser {}, idFriend {}", idUser, idFriend);
                throw new ValidationException("Пользователи не являются друзьями или у них нет общих друзей.");
            }
        } else {
            log.error("ID пользователей некорректны. ID: idUser {}, idFriend {}", idUser, idFriend);
            throw new ValidationException("ID пользователей некорректны.");
        }
    }

    public Collection<User> getFriends(Integer id) {
        if (id != null) {
            userStorage.findUserById(id);
            return friendshipRepository.getAllUserFriends(id).stream().toList();
        } else {
            log.error("ID не задан или не найден.");
            throw new ValidationException("ID пользователя не задан или некорректен.");
        }
    }
}