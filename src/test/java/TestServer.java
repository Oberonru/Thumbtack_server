import database.DataBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestServer {

    private Server server;

    @Before
    public void setupServer() {
        server = new Server();
    }

    @Test
    public void test_startServer() throws Exception {
        DataBase db = DataBase.getInstance();
        server.startServer("testServerData.json");
        Assert.assertEquals(db.getUserList().size(), 1);
        Assert.assertEquals(db.getSongList().size(), 2);
        Assert.assertEquals(db.getUserList().get(0).getFirstName(), "Uasya");
        Assert.assertEquals(db.getSongList().get(1).getSongName(), "second");
        Assert.assertEquals(db.getRatingList().get(0).getLogin(), "uaSek");
        Assert.assertEquals(db.getRatingList().get(0).getSongId(), 2);
    }

    @Test
    public void test_registerUser_serverIs_notStarted() throws Exception {
        Server server = new Server();
        String requestJsonString = "{\"firstName\":\"Boryaha\",\"lastName\":\"Morkovkin\",\"login\":\"boryan\"," +
                "\"password\":\"321\"}";
        Assert.assertEquals(server.registerUser(requestJsonString), "{\"error\":\"Server is not started!\"}");
    }

    //todo: надо ли делать тест в котором сам файл( сейчас testServerData.json") корявый? те данные в нём с ошибкой?

    @Test
    public void test_registerUser() throws Exception {
        DataBase db = DataBase.getInstance();
        server.startServer("testServerData.json");
        String registerUserJson = "{\"firstName\":\"Petro\",\"lastName\":\"First\",\"login\":\"petrucsho\"," +
                "\"password\":\"3432s3s\"}";
        server.registerUser(registerUserJson);
        Assert.assertEquals(db.getUserList().size(), 2);
        Assert.assertEquals(db.getUserList().get(1).getFirstName(), "Petro");
    }

    @Test
    public void test_registerUser_name_notValid() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"firstName\":\"B\",\"lastName\":\"Morkovkin\",\"login\":\"boryan\"," +
                "\"password\":\"321\"}";
        Assert.assertEquals(server.registerUser(requestJsonString), "{\"error\":\"Params is not valid\"}");
    }

    @Test
    public void test_registerUser_twoEqualsLogin() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"firstName\":\"Boryaha\",\"lastName\":\"Morkovkin\",\"login\":\"gamer\"," +
                "\"password\":\"321\"}";
        String registerJsonString2 = "{\"firstName\":\"Petro\",\"lastName\":\"First\",\"login\":\"gamer\"," +
                "\"password\":\"3432s3s\"}";
        server.registerUser(requestJsonString);
        Assert.assertEquals(server.registerUser(registerJsonString2), "{\"error\":\"Params is not valid\"}");
    }

    @Test
    public void test_logIn_failureFirstNameData() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"NAME\":\"Boryaha\",\"lastName\":\"Morkovkin\",\"login\":\"gamer\"," +
                "\"password\":\"321\"}";
        Assert.assertEquals("{\"error\":\"Request is not valid\"}", server.logIn(requestJsonString));
    }

    @Test
    public void test_logIn_failureLoginData() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"firstName\":\"Boryaha\",\"lastName\":\"Morkovkin\",\"log\":\"gamer\"," +
                "\"password\":\"321\"}";
        Assert.assertEquals("{\"error\":\"Request is not valid\"}", server.logIn(requestJsonString));
    }

    @Test
    public void test_logIn_failureToken() {
        //todo: не понятно как реализовать здесь проверку
        server.startServer("testServerData.json");
        String requestJsonString = "{\"firstName\":\"Boryaha\",\"lastName\":\"Morkovkin\",\"log\":\"gamer\"," +
                "\"password\":\"321\"}";
    }

    @Test
    public void test_addSong() throws Exception {
        DataBase db = DataBase.getInstance();
        server.startServer("testServerData.json");
        String requestLoginJsonString = "{\"login\" : \"uaSek\", \"password\" : \"123s\" }";
        server.logIn(requestLoginJsonString);
        String requestJsonString = "{\"songName\" : \"Musjaka\", \"composer\" : [\"Muzyak\", \"MuzyakMladshoi\"]," +
                " \"author\" : [\"OnYe\"], \"musician\" : \"MusyagGroup\",  \"songDuration\" : 0.8," +
                " \"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals(db.getSongList().size(), 2);
        Assert.assertEquals("{}", server.addSong(requestJsonString));
        Assert.assertEquals(db.getSongList().size(), 3);
    }

    @Test
    public void test_addSong_notLogined() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"songName\" : \"TucMuc\", \"composer\" : [\"Obdolbish\", \"Ukurish\"]," +
                " \"author\" : [\"Bomj\", \"Bezdar'\"], \"musician\" : \"Saruhanov\",  \"songDuration\" : 3.8," +
                " \"token\" : \"null\"}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.addSong(requestJsonString));
    }

    @Test
    public void test_addSong_loginIsFailure() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"songName\" : \"TucMuc\", \"composer\" : [\"Obdolbish\", \"Ukurish\"]," +
                " \"author\" : [\"Bomj\", \"Bezdar'\"], \"musician\" : \"Saruhanov\",  \"songDuration\" : 3.8," +
                " \"token\" : \"768757568-213f-4018-b61f-d4b1a0a78400\"}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.addSong(requestJsonString));
    }

    @Test
    public void test_addSong_logOut() throws Exception {
        DataBase db = DataBase.getInstance();
        server.startServer("testServerData.json");
        String requestLoginJsonString = "{\"login\" : \"uaSek\", \"password\" : \"123s\" }";
        server.logIn(requestLoginJsonString);
        String requestJsonString = "{\"songName\" : \"Musjaka\", \"composer\" : [\"Muzyak\", \"MuzyakMladshoi\"]," +
                " \"author\" : [\"OnYe\"], \"musician\" : \"MusyagGroup\",  \"songDuration\" : 0.8," +
                " \"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals(db.getSongList().size(), 2);
        server.addSong(requestJsonString);
        Assert.assertEquals(db.getSongList().size(), 3);
        String logOutJsonString = "{\"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals(server.logOut(logOutJsonString), "{}");
        String requestJsonString2 = "{\"songName\" : \"Musjaka\", \"composer\" : [\"Muzyak\", \"MuzyakMladshoi\"]," +
                " \"author\" : [\"OnYe\"], \"musician\" : \"MusyagGroup\",  \"songDuration\" : 0.8," +
                " \"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.addSong(requestJsonString2));
    }
}
