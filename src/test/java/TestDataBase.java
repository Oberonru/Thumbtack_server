import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImpl;
import database.DataBase;
import model.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestDataBase {

    @Test
    public void test_addUser() throws IOException {
        UserService userService = new UserService();
        UserDaoImpl userDao = new UserDaoImpl();
        User vasek = userService.createUserWithToken("Uasja", "Pupyakin", "Pupyan", "pup123");
        User boryan = userService.createUserWithToken("Boryaha", "Mordatii", "bormord", "8765");
        userDao.insert(vasek);
        userDao.insert(boryan);
        ObjectMapper mapper = new ObjectMapper();
        User[] deserializeUser = mapper.readValue(new File("test.txt"), User[].class);
        for (User u : deserializeUser) {
            System.out.println(u.getFirstName());
        }
    }

    //не совсем правильный тест
    @Test
    public void test_loadUserDataToCache() {
        DataBase db = DataBase.getInstance();
        UserService userService = new UserService();
        UserDaoImpl userDao = new UserDaoImpl();
        User vasek = userService.createUserWithToken("Uasja", "Pupyakin", "Pupyan", "pup123");
        User boryan = userService.createUserWithToken("Boryaha", "Mordatii", "bormord", "8765");
        userDao.insert(vasek);
        userDao.insert(boryan);
        db.loadUserDataToCache("test.txt");
        Assert.assertEquals(db.getUserList().size(), 2);
    }

    @Test
    //todo: по идее нужно стартануть сервер, добавить в него песни и остановить. Затем выполнить старт
    // todo: и автматически данные загрузятся из кэш..затем выполнять проверку
    public void test_loadSongDataToCache() throws Exception {
        Server server = new Server();
        server.startServer("test.txt", "songFile.txt");
        DataBase db = DataBase.getInstance();
        Assert.assertTrue(new File("test.txt").exists());
        Assert.assertTrue(new File("songFile.txt").exists());
        Assert.assertEquals(db.getSongList().size(), 2);
        Assert.assertTrue(db.getSongList().get(0).getSongName().equals("Elochka"));
        Assert.assertTrue(db.getSongList().get(1).getSongName().equals("second"));

    }
}
