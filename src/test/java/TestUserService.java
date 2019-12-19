import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.junit.Assert;
import org.junit.Test;

public class TestUserService {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    //todo:как протестировать, что генерирует?
    public void test_generateId() throws Exception {
        UserService userService = new UserService();
        String json = userService.registerUser("{\"firstName\":\"Uasya\",\"lastName\":\"Pupkin\"," +
                "\"login\":\"uaSek\",\"password\":\"123s\"}");
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
        Server server = new Server();
        server.startServer("test.txt");
        //,..всё тип топ, сервер стартанул у бд есть список пользователей из которого можно извлечь требуемое поле и тд..
        String requestJsonString = "{\"firstName\":\"Vaska\",\"lastName\":\"Pupkin\",\"login\":\"vasilii\"," +
                "\"password\":\"123s\"}";
        String requestJsonString2 = "{\"firstName\":\"Petro\",\"lastName\":\"First\",\"login\":\"petrucsho\"," +
                "\"password\":\"3432s3s\"}";
        String jsonResponse = userService.registerUser(requestJsonString);
        String jsonResponse2 = userService.registerUser(requestJsonString2);
        //todo: тут нужно делать какую то проверку

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
        Assert.assertEquals(jsonResponse2, "{\"error\":\"login is already defined\"}");
    }

    @Test
    public void test_logOut() throws Exception {
        Server server = new Server();
        server.startServer("test.txt");
        UserService userService = new UserService();
        String requestJsonString = "{\"token\" : \"68df9475-5b99-4183-af04-5a9ff380976f\"}";
        Assert.assertTrue(userService.logOut(requestJsonString));
    }

    //отрабатывает правильно, меняет логин, а как проверять?
    @Test
    public void test_logIn() throws Exception {
        UserService userService = new UserService();
        Server server = new Server();
        server.startServer("test.txt");
        String requestJsonString = "{\"login\" : \"vasilii\", \"password\" : \"123s\"}";
        userService.logIn(requestJsonString);
    }
}
