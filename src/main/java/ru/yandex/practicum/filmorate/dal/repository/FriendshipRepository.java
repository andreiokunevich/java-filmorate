package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Repository
public class FriendshipRepository extends BaseRepository<User> {

    private static final String ADD_FRIEND_QUERY = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
    private static final String REMOVE_FRIEND_QUERY = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String COMMON_FRIENDS_QUERY = "SELECT * FROM users WHERE user_id IN " +
            "(SELECT friend_id FROM friendship WHERE user_id = ?) AND user_id IN " +
            "(SELECT friend_id FROM friendship WHERE user_id = ?)";
    private static final String ALL_FRIENDS_USER_QUERY = "SELECT * FROM users WHERE user_id IN " +
            "(SELECT friend_id FROM friendship WHERE user_id = ?)";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public void addFriend(Integer idUser, Integer idFriend) {
        insertKeys(ADD_FRIEND_QUERY, idUser, idFriend);
    }

    public void deleteFriend(Integer idUser, Integer idFriend) {
        delete(REMOVE_FRIEND_QUERY, idUser, idFriend);
    }

    public Collection<User> getCommonFriends(Integer idUser, Integer idUserTwo) {
        return findMany(COMMON_FRIENDS_QUERY, idUser, idUserTwo);
    }

    public Collection<User> getAllUserFriends(Integer id) {
        return findMany(ALL_FRIENDS_USER_QUERY, id);
    }
}