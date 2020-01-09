import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.junit.Assert;
import org.junit.Test;
import request.LogInDtoRequest;

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

    //отрабатывает правильно, меняет логин, а как проверять?
    @Test
    public void test_logIn() throws Exception {
        UserService userService = new UserService();
        Server server = new Server();
        server.startServer("testServerData.json");
        String requestJsonString = "{\"login\" : \"vasilii\", \"password\" : \"123s\"}";
        LogInDtoRequest request = mapper.readValue(requestJsonString, LogInDtoRequest.class);
        userService.logIn(request);
    }

    @Test
    public void test_logOut() throws Exception {
        Server server = new Server();
        server.startServer("testServerData.json");
        UserService userService = new UserService();
        String requestJsonString = "{\"token\" : \"3c97e287-d38e-43ce-9de2-7c192b98a423\"}";
    }


}
