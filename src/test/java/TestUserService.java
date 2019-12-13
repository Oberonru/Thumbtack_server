import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.junit.Assert;
import org.junit.Test;
import response.RegisterUserDtoResponse;

public class TestUserService {
    @Test
    //todo:как протестировать, что генерирует?
    public void test_generateId() {
        UserService userService = new UserService();
    }

    @Test
    public void test_createUser() {
        UserService userService = new UserService();
        User user = userService.createUserWithToken("Uasya", "Pupkin", "vasek", "12345");
        Assert.assertEquals(user.getFirstName(), "Uasya");
        Assert.assertEquals(user.getLastName(), "Pupkin");
        Assert.assertEquals(user.getLogin(), "vasek");
        Assert.assertEquals(user.getPassword(), "12345");
    }

    @Test
    public void test_registerUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        UserService userService = new UserService();
        String jsonRequest = "{\"firstName\":\"Uasya\",\"lastName\":\"Pupkin\",\"login\":\"uaSek\",\"password\":\"123s\"}";
        String jsonResponse = userService.registerUser(jsonRequest);
        RegisterUserDtoResponse dtoResponse = mapper.readValue(jsonResponse, RegisterUserDtoResponse.class);
        Assert.assertEquals(jsonResponse, dtoResponse.getToken());
    }
}
