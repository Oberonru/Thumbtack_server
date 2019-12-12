package database;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private List<User> userList = new ArrayList<User>();
    //добавление сразу с сохраниением в файл
    public void addUser(User user) throws IOException {
        userList.add(user);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("C:\\projects\\testData.txt"), userList);
    }

    public List<User> getUserList() {
        return userList;
    }


}
