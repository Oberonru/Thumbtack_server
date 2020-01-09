package dao;

import database.DataBase;
import model.User;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private DataBase db = DataBase.getInstance();

    public void insert(User user) {
        try {
            db.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            db.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getUserList() {
        return db.getUserList();
    }
}
