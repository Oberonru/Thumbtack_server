import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSongService {
    private Server server;
    private SongService songService;

    @Before
    public void setupServer() {
        server = new Server();
        songService = new SongService();
        server.startServer("songTest.json");
    }

    @Test
    public void test_deleteSong() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"songId\" : 1}"; //gamut удаляет свою песню
        Assert.assertEquals("\"{}\"", server.deleteSong(requestJsonString));
    }

    @Test
    public void test_deleteSong_someoneElseSong() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"songId\" : 3}";
        Assert.assertEquals("{\"error\":\"The user can't delete song\"}", server.deleteSong(requestJsonString));
    }

    @Test
    public void test_deleteSong_invalidToken() throws Exception {
        String requestJsonString = "{\"token\" : \"I Snova FIG\", \"songId\" : 1}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.deleteSong(requestJsonString));
    }

    @Test
    public void test_deleteSong_moreOneRate() throws Exception {
        String requestJsonString = "{\"token\" : \"9f0e256-e429-4eec-a24a-4b2901eb00000\", \"songId\" : 3}";
        Assert.assertEquals("{\"error\":\"Song rating is deleted, the user can't delete song\"}",
                server.deleteSong(requestJsonString));
        server.stopServer("saveSongTest.json");
    }

    /**
     * !!!!!!!!!!!!!
     * В условии ничего не сказано про удаление песни автором предложения, у которой есть уже комментарий...
     * Сделал так же как и с удалением песни у котророй более одного рейтинга, те удаляется рейтинг пользователя,
     * а песня остается
     */
    @Test
    public void test_deleteSong_withComment() throws Exception {
        String requestJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\", \"songId\" : 4}";
        Assert.assertEquals("{\"error\":\"Song rating is deleted, the user can't delete song\"}",
                server.deleteSong(requestJsonString));
        server.stopServer("saveSongTest.json");
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
    public void test_findSongByComposers_tokenNotValid() throws Exception {
        String requestJsonString = "{\"token\" : \"ZERO\", \"composers\" : [\"Zayac\", \"Volk\"]}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.getSongByComposers(requestJsonString));
    }

    @Test
    public void test_getSongByAutors() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"author\" : [\"RumuMburum\"]}";
        System.out.println(server.getSongByAuthors(requestJsonString));
    }

    @Test
    public void test_getSongByAutors_notExsist() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"author\" : [\"NO\"]}";
        Assert.assertEquals("{\"error\":\"Authors is not found\"}", server.getSongByAuthors(requestJsonString));
    }

    @Test
    public void test_getSongByAutors_tokenNotValid() throws Exception {
        String requestJsonString = "{\"token\" : \"NONE\", \"author\" : [\"RumuMburum\"]}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.getSongByAuthors(requestJsonString));
    }

    @Test
    public void test_getSongByMusician() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\", \"musician\" : \"capel'\"}";
        System.out.println(server.getSongByMusician(requestJsonString));
    }

    @Test
    public void test_getSongByMusician_tokenNotValid() throws Exception {
        String requestJsonString = "{\"token\" : \"NOT Valid\", \"musician\" : \"Capel'\"}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.getSongByMusician(requestJsonString));
    }


}
