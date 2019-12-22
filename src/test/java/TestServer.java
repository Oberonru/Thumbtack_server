import database.DataBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class TestServer {

    @Test
    public void test_startServer_validFileData() throws Exception {
        Server server = new Server();
        DataBase db = DataBase.getInstance();
        server.startServer("testServerData.txt", "testSongServiceData.txt");
        Assert.assertTrue(new File("testServerData.txt").exists());
        Assert.assertEquals(db.getUserList().size(), 3);
        Assert.assertEquals(db.getUserList().get(2).getLogin(), "petrucsho");
        Assert.assertEquals(db.getUserList().get(1).getFirstName(), "Vaska");
        Assert.assertTrue(new File("testSongServiceData.txt").exists());
        Assert.assertEquals(db.getSongList().size(), 2);
        Assert.assertTrue(db.getSongList().get(0).getSongName().equals("Elochka"));
        Assert.assertTrue(db.getSongList().get(1).getSongName().equals("second"));
    }

    @Test
    public void test_startServer_userFileIs_NULL() throws Exception {
        Server server = new Server();
        DataBase db = DataBase.getInstance();
        server.startServer(null, "testSongServiceData.txt");
        Assert.assertEquals(db.getUserList().size(), 0);
        Assert.assertEquals(db.getSongList().size(), 2);
        Assert.assertEquals(db.getSongList().get(0).getToken(), "09b7c049-fbc8-4d6b-8b52-8ca2fd5f6714");
    }
//Тесты перенес с TestUserService
    @Test
    public void test_registerUser() throws Exception {
        UserService userService = new UserService();
        Server server = new Server();
        server.startServer("testUserService.txt", "testSongServiceData.txt");
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

//Тесты перенес с TestUserService

    //Тест при первом запуске, добавляет нового пользователя и возвращает ответ жсон токен
    //при повторном запросе выдаёт ошибки в виде жсон...........как вот это реализовать в тесте
    @Test
    public  void tesr_registerUserService() throws Exception {
        Server server = new Server();
        server.startServer("testServerData.txt", "testSongServiceData.txt");
        String requestJsonString = "{\"firstName\":\"Osya\",\"lastName\":\"Bender\",\"login\":\"bendOsya\"," +
                "\"password\":\"9999999\"}";
        String serverResponse = server.registerUser(requestJsonString);
        System.out.println(serverResponse);
    }
    @Test
    public  void tesr_registerUserService_notStarted() throws Exception {
        Server server = new Server();
        String requestJsonString = "{\"firstName\":\"Osya\",\"lastName\":\"Bender\",\"login\":\"bendOsya\"," +
                "\"password\":\"9999999\"}";
        Assert.assertEquals("{\"error\":\"Server is not started!\"}", server.registerUser(requestJsonString));
    }
}
