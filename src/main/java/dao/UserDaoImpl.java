package dao;

import model.User;
import database.DataBase;
public class UserDaoImpl implements UserDao {

    private DataBase dataBase = new DataBase();

    public void insert(User user) {
        dataBase.addUser(user);
    }
}
