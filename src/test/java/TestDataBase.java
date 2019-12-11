import database.DataBase;
import model.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class TestDataBase {
    @Test
    public void test_addUser() throws Exception {
        DataBase db = new DataBase();
        Assert.assertEquals(db.getUserList().size(), 0);
        User user = new User();
        user.setFirstName("Ijora");
        user.setLastName("Zajratii");
        user.setLogin("Zajrati_ijora");
        user.setPassword("0000");
        db.addUser(user);
        Assert.assertEquals(db.getUserList().size(), 1);
        for (User u : db.getUserList()) {
            Assert.assertEquals(u.getFirstName(), "Ijora");
            Assert.assertEquals(u.getLastName(), "Zajratii");
            Assert.assertEquals(u.getLogin(), "Zajrati_ijora");
            Assert.assertEquals(u.getPassword(), "0000");
        }
        //verify serialize to file
        Assert.assertTrue(new File("test.txt").exists());
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("test.txt")));
        User deserializableUser = (User) ois.readObject();
    }

}
