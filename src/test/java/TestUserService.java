import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.junit.Assert;
import org.junit.Test;

public class TestUserService {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    //todo:как протестировать, что генерирует?
    public void test_generateId() {
        UserService userService = new UserService();
    }

    @Test
    public void test_createUserWithToken() {
        UserService userService = new UserService();
        User user = userService.createUserWithToken("Uasya", "Pupkin", "vasek", "12345");
        Assert.assertEquals(user.getFirstName(), "Uasya");
        Assert.assertEquals(user.getLastName(), "Pupkin");
        Assert.assertEquals(user.getLogin(), "vasek");
        Assert.assertEquals(user.getPassword(), "12345");
    }

    @Test
    public void test_registerUser() throws Exception {
        UserService userService = new UserService();
        String requestJsonString = "{\"firstName\":\"Uasya\",\"lastName\":\"Pupkin\",\"login\":\"uaSek\",\"password\":\"123s\"}";
        String jsonResponse = userService.registerUser(requestJsonString);
        //todo:как вытащить токен для проверки?
        System.out.println(jsonResponse);
    }

    @Test
    public void test_registerUser_invalidName() throws Exception {
        UserService userService = new UserService();
        String requestJsonString =
                "{\"firstName\":\"U\",\"lastName\":\"Pupkin\",\"login\":\"uaSek\",\"password\":\"123s\"}";
        String jsonResponse = userService.registerUser(requestJsonString);
        Assert.assertEquals("{\"error\":\"Params isn't valid\"}", jsonResponse);
    }

    @Test
    public void test_registerUser_twoEqualsLogin() throws Exception {
        UserService userService = new UserService();
        String requestJsonString = "{\"firstName\":\"Uasya\",\"lastName\":\"Pupkin\",\"login\":\"uaSek\"," +
                "\"password\":\"123s\"}";
        String requestJsonString2 = "{\"firstName\":\"Vasyuha\",\"lastName\":\"Maklai\",\"login\":\"uaSek\"," +
                "\"password\":\"yt2\"}";

        String jsonResponse = userService.registerUser(requestJsonString);
        String jsonResponse2 = userService.registerUser(requestJsonString2);
        System.out.println(jsonResponse);
        System.out.println(jsonResponse2);

    }

}
