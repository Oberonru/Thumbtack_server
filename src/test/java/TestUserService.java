import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import request.LogInDtoRequest;
import request.RegisterUserDtoRequest;

public class TestUserService {
    UserService userService;
    ObjectMapper mapper;

    @Before
    public void setupUsersrvice() {
        Server server = new Server();
        server.startServer("testServerData.json");
        mapper = new ObjectMapper();
        userService = new UserService();
    }

   @Test
   public void test_registerUser() throws Exception {
       String registerUserJson = "{\"firstName\":\"Petro\",\"lastName\":\"First\",\"login\":\"petrucsho\"," +
               "\"password\":\"3432s3s\"}";
       RegisterUserDtoRequest request = mapper.readValue(registerUserJson, RegisterUserDtoRequest.class);
       Assert.assertEquals(36, userService.registerUser(request).length());
   }
   @Test
   public void test_registerUser_twoEqualsLogin() throws Exception {
       String requestJsonString = "{\"firstName\":\"Petro\",\"lastName\":\"First\",\"login\":\"petrucsho\"," +
               "\"password\":\"3432s3s\"}";
       RegisterUserDtoRequest request = mapper.readValue(requestJsonString, RegisterUserDtoRequest.class);
       Assert.assertEquals(36, userService.registerUser(request).length());
       String requestJsonString2 = "{\"firstName\":\"Boryaha\",\"lastName\":\"Morkovkin\",\"login\":\"petrucsho\"," +
               "\"password\":\"NNN34\"}";
       RegisterUserDtoRequest request2 = mapper.readValue(requestJsonString2, RegisterUserDtoRequest.class);
       try {
           userService.registerUser(request2);
       }catch (Exception e) {
           Assert.assertEquals("Login is already used", e.getMessage());
       }
   }

    @Test
    public void test_logIn() throws Exception {
        String requestJsonString = "{\"login\" : \"uaSek\", \"password\" : \"123s\"}";
        LogInDtoRequest request = mapper.readValue(requestJsonString, LogInDtoRequest.class);
        Assert.assertEquals("{}", userService.logIn(request));
    }

    @Test
    public void test_logOut() throws Exception {
        String requestJsonString = "{\"token\" : \"3c97e287-d38e-43ce-9de2-7c192b98a423\"}";
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
}
