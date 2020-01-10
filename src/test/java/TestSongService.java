import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import response.ErrorDtoResponse;

public class TestSongService {
    private Server server;
    private SongService songService;

    @Before
    public void setupServer() {
        server = new Server();
        songService = new SongService();
        server.startServer("songTest.json");
    }

//    @Test
//    public void test_addSong() throws Exception {
//        String requestJsonString = "{\"songName\" : \"Murka\", \"composer\" : [\"Mur\", \"Mureh\"]," +
//                " \"author\" : [\"None\", \"None\"], \"musician\" : \"Ruhum\",  \"songDuration\" : 1," +
//                " \"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\"}";
//        Assert.assertEquals(songService.addSong(requestJsonString), "{}");
//        server.stopServer("saveSongTest.json");
//    }

    //    @Test
//    public void test_addSong_invalidToken() throws Exception {
//        String requestJsonString = "{\"songName\" : \"Elochka\", \"composer\" : [\"Zayac\", \"Volk\"]," +
//                " \"author\" : [\"Volk\", \"Zayac\"], \"musician\" : \"Capel'\",  \"songDuration\" : 5," +
//                " \"token\" : \"FIG\"}";
//        Assert.assertEquals("{\"error\":\"User not found\"}", songService.addSong(requestJsonString));
//    }
    @Test
    public void test_deleteSong() throws Exception {
        String requestJsonString = "{\"token\" : \"9f0e256-e429-4eec-a24a-4b2901eb00000\", \"songId\" : 3}";
        Assert.assertEquals("\"{}\"", server.deleteSong(requestJsonString));
    }

    @Test
    public void test_deleteSong_someoneElseSong() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"songId\" : 2}";
        Assert.assertEquals("{\"error\":\"The user can't delete song\"}", server.deleteSong(requestJsonString));
    }

    @Test
    public void test_deleteSong_invalidToken() throws Exception {
        String requestJsonString = "{\"token\" : \"I Snova FIG\", \"songId\" : 1}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.deleteSong(requestJsonString));
    }

    @Test
    public void test_deleteSong_moreOneRate() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"songId\" : 1}";
        Assert.assertEquals("{\"error\":\"The user can't delete song\"}", server.deleteSong(requestJsonString));
        server.stopServer("saveSongTest.json");
    }

    @Test
    public void test_deleteSong_withComment() throws Exception {
        String requestJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\", \"songId\" : 4}";
        Assert.assertEquals("{\"error\":\"The user can't delete song\"}", server.deleteSong(requestJsonString));
    }

    @Test
    public void test_findSongByComposers_1() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"composers\" : [\"Zayac\", \"Volk\"]}";
        System.out.println(server.getSongByComposers(requestJsonString));
    }

    @Test
    public void test_findSongByComposers_2() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"composers\" : [\"Zayac\"]}";
        System.out.println(server.getSongByComposers(requestJsonString));
    }

    @Test
    public void test_findSongByComposers_3() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"composers\" : [\"Zayac\"]}";
        System.out.println(server.getSongByComposers(requestJsonString));
    }

    @Test
    public void test_findSongByComposers_invalidToken() throws Exception {
        String requestJsonString = "{\"token\" : \"ZERO\", \"composers\" : [\"Zayac\", \"Volk\"]}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.getSongByComposers(requestJsonString));
    }

    @Test
    public void test_getSongs() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\"}";
        System.out.println(server.getSongs(requestJsonString));
    }

    @Test
    public void test_getSongs_invalidToken() throws Exception {
        String requestJsonString = "{\"token\" : \"wwww\"}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.getSongs(requestJsonString));
    }
}
