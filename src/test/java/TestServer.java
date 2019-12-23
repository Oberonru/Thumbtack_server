import database.DataBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

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
    }

    @Test
    public void test_registerUser() throws Exception {
        Server server = new Server();
        DataBase db = DataBase.getInstance();
        server.startServer("testServerData.json");
        String registerUserJson = "{\"firstName\":\"Petro\",\"lastName\":\"First\",\"login\":\"petrucsho\"," +
                "\"password\":\"3432s3s\"}";
        String tokenResponse = server.registerUser(registerUserJson);
        Assert.assertEquals(db.getUserList().size(), 2);
        Assert.assertEquals(db.getUserList().get(1).getFirstName(), "Petro");
    }

    @Test
    public void test_registerUser_paramIs_notValid() throws Exception {
        Server server = new Server();
        server.startServer("testServerData.json");
        String requestJsonString = "{\"firstName\":\"B\",\"lastName\":\"Morkovkin\",\"login\":\"boryan\"," +
                "\"password\":\"321\"}";
        //todo: отдельно по каждому запросу должен выдавать свою ошибку
        System.out.println(server.registerUser(requestJsonString));
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
        server.logIn(requestLoginJsonString);
        String requestJsonString = "{\"songName\" : \"Musjaka\", \"composer\" : [\"Muzyak\", \"MuzyakMladshoi\"]," +
                " \"author\" : [\"OnYe\"], \"musician\" : \"MusyagGroup\",  \"songDuration\" : 0.8," +
                " \"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals(db.getSongList().size(), 2);
        server.addSong(requestJsonString);
        Assert.assertEquals(db.getSongList().size(), 3);
    }

    @Test
    public void test_addSong_notLogined() throws Exception {
        Server server = new Server();
        server.startServer("testServerData.json");
        String requestJsonString = "{\"songName\" : \"TucMuc\", \"composer\" : [\"Obdolbish\", \"Ukurish\"]," +
                " \"author\" : [\"Bomj\", \"Bezdar'\"], \"musician\" : \"Saruhanov\",  \"songDuration\" : 3.8," +
                " \"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals(server.addSong(requestJsonString), "{\"errror\" : \"login not found\"}");
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
        server.logOut(logOutJsonString);
        server.stopServer("saveData.json");
        Assert.assertEquals(server.addSong(requestJsonString), "{\"errror\" : \"login not found\"}");
    }


}
