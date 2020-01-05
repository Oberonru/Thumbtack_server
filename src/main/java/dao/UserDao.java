package dao;

import model.User;

import java.util.List;

public interface UserDao {
    void insert(User user);
    void updateUser(User user);
    User getUserByToken(String token);
    List<User> getUserList();
}
