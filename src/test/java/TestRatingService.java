import database.DataBase;
import org.junit.Assert;
import org.junit.Test;

public class TestRatingService {
    @Test
    public void test_addRating() throws Exception {
        Server server = new Server();
        DataBase db = DataBase.getInstance();
        server.startServer("ratingTest.json");
        String requestJsonString = "{\"login\" : \"gamut\", \"songId\" : 1, \"songRaiting\" : 4}";
        Assert.assertEquals("{}", server.addRaiting(requestJsonString));
        server.stopServer("saveRatingTest.json");
    }

    @Test
    public void test_deleteRating() throws Exception {
        Server server = new Server();
        server.startServer("ratingTest.json");
        String requestJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\", \"songId\" : 1}";
        Assert.assertEquals("{}", server.deleteRaiting(requestJsonString));
    }

    @Test
    public void test_deleteRaiting_incorrectToken() throws Exception {
        Server server = new Server();
        server.startServer("ratingTest.json");
        String requestJsonString = "{\"token\" : \"fig2020\", \"songId\" : 1}";
        Assert.assertEquals("\"error\" : \"user not found\"", server.deleteRaiting(requestJsonString));
    }

    @Test
    public void test_deleteRating_incorrectSongId() throws Exception {
        Server server = new Server();
        server.startServer("ratingTest.json");
        String requestJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\", \"songId\" : -100}";
        Assert.assertEquals("\"error\" : \"Song not found\"" ,server.deleteRaiting(requestJsonString));
    }

    @Test
    public void test_deleteRating_byLoggedOutUser() throws Exception {
        Server server = new Server();
        server.startServer("ratingTest.json");
        server.logOut("{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\"}");
        String requestJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\", \"songId\" : 1}";
        System.out.println(server.deleteRaiting(requestJsonString));
        Assert.assertEquals("\"error\" : \"user not found\"", server.deleteRaiting(requestJsonString));
    }
}
