import model.User;
import org.junit.Assert;
import org.junit.Test;

public class TestUserService {
    @Test
    //todo:как протестировать, что генерирует?
    public void test_generateId() {
        UserService userService = new UserService();
    }

    @Test
    public void test_createUser() {
        UserService userService = new UserService();
        User user =  userService.createUser("Uasya", "Pupkin", "vasek", "12345");
        Assert.assertEquals(user.getFirstName(), "Uasya");
        Assert.assertEquals(user.getLastName(),"Pupkin");
        Assert.assertEquals(user.getLogin(), "vasek");
        Assert.assertEquals(user.getPassword(), "12345");
    }
}
