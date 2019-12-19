import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
import org.junit.Assert;
import org.junit.Test;

public class TestSongService {
    ObjectMapper mapper = new ObjectMapper();
    DataBase db = DataBase.getInstance();

    @Test
    public void test_addSong() throws Exception {
        Server server = new Server();
        server.startServer("test.txt");
        SongService songService = new SongService();
        String requestJsonString = "{\"songName\" : \"Elochka\", \"composer\" : [\"Zayac\", \"Volk\"]," +
                " \"author\" : [\"Volk\", \"Zayac\"], \"musician\" : \"Capel'\",  \"songDuration\" : 5," +
                " \"token\" : \"09b7c049-fbc8-4d6b-8b52-8ca2fd5f6714\"}";
        String response = songService.addSong(requestJsonString);
        Assert.assertEquals(response, "{}");
    }
}
