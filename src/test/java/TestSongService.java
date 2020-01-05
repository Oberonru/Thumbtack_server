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
        server.startServer("songTest.json");
        String requestJsonString = "{\"songName\" : \"Murka\", \"composer\" : [\"Mur\", \"Mureh\"]," +
                " \"author\" : [\"None\", \"None\"], \"musician\" : \"Ruhum\",  \"songDuration\" : 1," +
                " \"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\"}";
        Assert.assertEquals(songService.addSong(requestJsonString), "{}");
        server.stopServer("saveSongTest.json");
    }

    @Test
    public void test_addSong_invalidToken() throws Exception {
        server.startServer("songTest.json");
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
