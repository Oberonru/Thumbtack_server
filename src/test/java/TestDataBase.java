import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImpl;
import database.DataBase;
import model.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TestDataBase {

    @Test
    public void test_addUser() throws IOException {
        UserService userService = new UserService();
        User vasek = userService.createUser("Uasja", "Pupyakin", "Pupyan","pup123");
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.insert(vasek);

        //Десериализация пользователя
        ObjectMapper mapper = new ObjectMapper();
        User deserializeUser = mapper.readValue("test.txt", User.class);
        Assert.assertEquals(deserializeUser.getFirstName(), "Uasja");

    }
}
