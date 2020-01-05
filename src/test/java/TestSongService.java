import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
import org.junit.Assert;
import org.junit.Test;

public class TestSongService {
    ObjectMapper mapper = new ObjectMapper();
    DataBase db = DataBase.getInstance();
    Server server = new Server();
    SongService songService = new SongService();

    @Test
    public void test_addSong() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"songName\" : \"second\", \"composer\" : [\"BBBB\", \"AAAA\"]," +
                " \"author\" : [\"CCC\", \"DDD\"], \"musician\" : \"Songer\",  \"songDuration\" : 1," +
                " \"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\"}";
        Assert.assertEquals(songService.addSong(requestJsonString), "{}");
    }

    @Test
    public void test_addSong_invalidToken() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"songName\" : \"Elochka\", \"composer\" : [\"Zayac\", \"Volk\"]," +
                " \"author\" : [\"Volk\", \"Zayac\"], \"musician\" : \"Capel'\",  \"songDuration\" : 5," +
                " \"token\" : \"09b7c049-fbc8-4d6b-8b52-8ca2fd5f6734\"}";

        Assert.assertEquals(songService.addSong(requestJsonString), "{\"error\" : \"User not found\"}");
    }

    @Test
    public void test_deleteSong_someoneElseseSong() throws Exception {
        Server server = new Server();
        server.startServer("songTest.json");
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"songId\" : 2}";
        Assert.assertEquals("{\"error\" : \"The user can't delete song\"}",server.deleteSong(requestJsonString));
    }
    @Test
    public void test_deleteSong_moreOneRate() throws Exception {
        Server server = new Server();
        server.startServer("songTest.json");
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"songId\" : 1}";
        Assert.assertEquals("{\"error\" : \"The user can't delete song\"}", server.deleteSong(requestJsonString));
        server.stopServer("saveSongTest.json");
    }
    @Test
    public void test_deleteSong() throws Exception {
        Server server = new Server();
        server.startServer("songTest.json");
        String requestJsonString = "{\"token\" : \"9f0e256-e429-4eec-a24a-4b2901eb00000\", \"songId\" : 3}";
        Assert.assertEquals("{}", server.deleteSong(requestJsonString));
        server.stopServer("saveSongTest.json");
    }


//    @Test
//    public void test_frequencyRaitings() {
//        Server server = new Server();
//        server.startServer("songTest.json");
//        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"songId\" : 1}";
//    }

}
