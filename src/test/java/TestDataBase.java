import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImpl;
import model.User;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestDataBase {

    @Test
    public void test_addUser() throws IOException {
        UserService userService = new UserService();
        UserDaoImpl userDao = new UserDaoImpl();
        User vasek = userService.createUserWithToken("Uasja", "Pupyakin", "Pupyan","pup123");
        User boryan = userService.createUserWithToken("Boryaha", "Mordatii","bormord", "8765");
        userDao.insert(vasek);
        userDao.insert(boryan);
        ObjectMapper mapper = new ObjectMapper();
        User[] deserializeUser = mapper.readValue(new File("test.txt"), User[].class);
        for (User u : deserializeUser) {
            System.out.println(u.getFirstName());
        }
    }


}
