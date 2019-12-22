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
                " \"token\" : \"61b51fca-6100-421e-b829-1a9da83faae3\"}";
        String response = songService.addSong(requestJsonString);
        Assert.assertEquals(response, "{}");
    }

    @Test
    public void test_addSong_invalidToken() throws Exception {
        server.startServer("testServerData.json");
        String requestJsonString = "{\"songName\" : \"Elochka\", \"composer\" : [\"Zayac\", \"Volk\"]," +
                " \"author\" : [\"Volk\", \"Zayac\"], \"musician\" : \"Capel'\",  \"songDuration\" : 5," +
                " \"token\" : \"09b7c049-fbc8-4d6b-8b52-8ca2fd5f6734\"}";
        String response = songService.addSong(requestJsonString);
        Assert.assertEquals(response, "{\"error\":\"Token in invalid\"}");
    }
}
