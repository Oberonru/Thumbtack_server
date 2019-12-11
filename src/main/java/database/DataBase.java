package database;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class DataBase {

    private List<User> userList = new ArrayList<User>();

    //добавление сразу с сохраниением в файл
    public void addUser(User user) {
        userList.add(user);
        try {
            ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(new File("test.txt")));
            ous.writeObject(userList);
            ous.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public List<User> getUserList() {
        return userList;
    }


}
