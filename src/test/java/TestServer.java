import database.DataBase;
import org.junit.Assert;
import org.junit.Test;

public class TestServer {
    @Test
    public void test_registerUser_serverIs_notStarted() throws Exception {
        Server server = new Server();
        String requestJsonString = "{\"firstName\":\"Boryaha\",\"lastName\":\"Morkovkin\",\"login\":\"boryan\"," +
                "\"password\":\"321\"}";
        Assert.assertEquals(server.registerUser(requestJsonString), "{\"error\":\"Server is not started!\"}");
    }

    @Test
    public void test_startServer() {
        DataBase db = DataBase.getInstance();
        Server server = new Server();
        server.startServer("testServerData.json");
        Assert.assertEquals(db.getUserList().size(), 1);
        Assert.assertEquals(db.getSongList().size(), 2);
        Assert.assertEquals(db.getUserList().get(0).getFirstName(), "Uasya");
        Assert.assertEquals(db.getSongList().get(1).getSongName(), "second");
        Assert.assertEquals(db.getRaitingList().get(0).getToken(), "61b51fca-6100-421e-b829-1a9da83faae3");
        Assert.assertEquals(db.getRaitingList().get(0).getSongId(), 2);
    }

    //todo: надо ли делать тест в котором сам файл( сейчас testServerData.json") корявый? те данные в нём с ошибкой?

    @Test
    public void test_registerUser() throws Exception {
        Server server = new Server();
        DataBase db = DataBase.getInstance();
        server.startServer("testServerData.json");
        String registerUserJson = "{\"firstName\":\"Petro\",\"lastName\":\"First\",\"login\":\"petrucsho\"," +
                "\"password\":\"3432s3s\"}";
        server.registerUser(registerUserJson);
        Assert.assertEquals(db.getUserList().size(), 2);
        Assert.assertEquals(db.getUserList().get(1).getFirstName(), "Petro");
    }

    @Test
    public void test_registerUser_paramIs_notValid() throws Exception {
        Server server = new Server();
        server.startServer("testServerData.json");
        String requestJsonString = "{\"firstName\":\"B\",\"lastName\":\"Morkovkin\",\"login\":\"boryan\"," +
                "\"password\":\"321\"}";
        Assert.assertEquals(server.registerUser(requestJsonString), "{\"error\":\"Params is not valid\"}");
    }

    @Test
    public void test_registerUser_twoEqualsLogin() throws Exception {
        Server server = new Server();
        server.startServer("testServerData.json");
        String requestJsonString = "{\"firstName\":\"Boryaha\",\"lastName\":\"Morkovkin\",\"login\":\"gamer\"," +
                "\"password\":\"321\"}";
        String registerJsonString2 = "{\"firstName\":\"Petro\",\"lastName\":\"First\",\"login\":\"gamer\"," +
                "\"password\":\"3432s3s\"}";
        server.registerUser(requestJsonString);
        System.out.println(server.registerUser(registerJsonString2));
    }

    @Test
    public void test_addSong() throws Exception {
        Server server = new Server();
        DataBase db = DataBase.getInstance();
        server.startServer("testServerData.json");
        String requestLoginJsonString = "{\"login\" : \"uaSek\", \"password\" : \"123s\" }";
        String tokenString = server.logIn(requestLoginJsonString);
        String requestJsonString = "{\"songName\" : \"Musjaka\", \"composer\" : [\"Muzyak\", \"MuzyakMladshoi\"]," +
                " \"author\" : [\"OnYe\"], \"musician\" : \"MusyagGroup\",  \"songDuration\" : 0.8," +
                " \"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals(db.getSongList().size(), 2);
        System.out.println(server.addSong(requestJsonString));
        Assert.assertEquals(db.getSongList().size(), 3);
    }

    @Test
    public void test_addSong_notLogined() throws Exception {
        Server server = new Server();
        server.startServer("testServerData.json");
        String requestJsonString = "{\"songName\" : \"TucMuc\", \"composer\" : [\"Obdolbish\", \"Ukurish\"]," +
                " \"author\" : [\"Bomj\", \"Bezdar'\"], \"musician\" : \"Saruhanov\",  \"songDuration\" : 3.8," +
                " \"token\" : \"null\"}";
        Assert.assertEquals(server.addSong(requestJsonString), "{\"error\" : \"User not found\"}");
    }

    @Test
    public void test_addSong_loginIsFailure() throws Exception {
        Server server = new Server();
        server.startServer("testServerData.json");
        String requestJsonString = "{\"songName\" : \"TucMuc\", \"composer\" : [\"Obdolbish\", \"Ukurish\"]," +
                " \"author\" : [\"Bomj\", \"Bezdar'\"], \"musician\" : \"Saruhanov\",  \"songDuration\" : 3.8," +
                " \"token\" : \"768757568-213f-4018-b61f-d4b1a0a78400\"}";
        Assert.assertEquals(server.addSong(requestJsonString), "{\"error\" : \"User not found\"}");
    }

    @Test
    public void test_addSong_logOut() throws Exception {
        Server server = new Server();
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
        String logOutJsonString = "{\"login\" : \"uaSek\"}";
        Assert.assertEquals(server.logOut(logOutJsonString), "{}");
        //нужно перезаписать токен
        //аааа.....в запросе неправильно, он уже измениться должен......как его записывать в этот запрос, что снизу
        //автоматически, не в рукопашую
        String requestJsonString2 = "{\"songName\" : \"Musjaka\", \"composer\" : [\"Muzyak\", \"MuzyakMladshoi\"]," +
                " \"author\" : [\"OnYe\"], \"musician\" : \"MusyagGroup\",  \"songDuration\" : 0.8," +
                " \"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals(server.addSong(requestJsonString2), "{\"error\" : \"User not found\"}");
    }
}
