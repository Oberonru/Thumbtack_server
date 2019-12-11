import org.junit.Assert;
import org.junit.Test;

public class TestServer {

    @Test
    public void test_registerUser() throws Exception {
        Server server = new Server();
        String jsonToRegistr = "{\"firstName\":\"Uasya\",\"lastName\":\"Pupkin\",\"login\":\"uaSek\",\"password\":\"123s\"}";
        System.out.println(server.registerUser(jsonToRegistr));
    }

    @Test
    public  void  test_registerUser_firstName_null() throws Exception {
        Server server = new Server();
        String jsonRequest = "{\"lastName\":\"Pupkin\",\"login\":\"uaSek\",\"password\":\"123s\"}";
        String jsonResponse =  server.registerUser(jsonRequest);
        Assert.assertEquals("{error}", jsonResponse);
    }

    @Test
    public void test_addSong() throws Exception {
        Server server = new Server();
        String jsonRequest = "{\"songName\":\"Lambada\", \"composer\": \"Baldei\"," +
                " \"author\":\"Gulyai\",\"musician\":\"Fredi\", \"songDuration\": 5, \"token\":\"123\"}}";
        String addSong = server.addSong(jsonRequest);
        Assert.assertEquals(addSong, "{}");

    }
}
