package dao;

import database.DataBase;
import model.User;

public class UserDaoImpl implements UserDao {

    private DataBase dataBase = DataBase.getInstance();

    public void insert(User user) {
        try {
            dataBase.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
