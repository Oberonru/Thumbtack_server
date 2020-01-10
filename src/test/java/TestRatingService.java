import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRatingService {
    private Server server;

    @Before
    public void setupServer() {
        server = new Server();
        server.startServer("ratingTest.json");
    }

    @Test
    public void test_addRating() throws Exception {
        String requestJsonString = "{\"login\" : \"gamut\", \"songId\" : 1, \"songRating\" : 2}";
        Assert.assertEquals("\"{}\"", server.addRating(requestJsonString));
        server.stopServer("saveRatingTest.json");
    }

    @Test
    public void test_addRating_rewriteRating() throws Exception {
        String requestJsonString = "{\"login\" : \"uaSek\", \"songId\" : 1, \"songRating\" : 3}";
        Assert.assertEquals("{\"error\":\"the author of the song can't change the rating\"}",
                server.addRating(requestJsonString));
    }

    @Test
    public void test_addRating_songIdNotFound() throws Exception {
        String requestJsonString = "{\"login\" : \"gamut\", \"songId\" : 999999999, \"songRating\" : 4}";
        Assert.assertEquals("{\"error\":\"Song not found\"}", server.addRating(requestJsonString));
    }

    @Test
    public void test_addRating_songRatingNotValid_1() throws Exception {
        String requestJsonString = "{\"login\" : \"gamut\", \"songId\" : 1, \"songRating\" : 0}";
        Assert.assertEquals("{\"error\":\"Rating not valid\"}", server.addRating(requestJsonString));
        server.stopServer("saveRatingTest.json");
    }

    @Test
    public void test_addRating_songRatingNotValid_2() throws Exception {
        String requestJsonString = "{\"login\" : \"gamut\", \"songId\" : 1, \"songRating\" : 6}";
        Assert.assertEquals("{\"error\":\"Rating not valid\"}", server.addRating(requestJsonString));
        server.stopServer("saveRatingTest.json");
    }

    @Test
    public void test_addRating_songRatingNotValid_3() throws Exception {
        String requestJsonString = "{\"login\" : \"gamut\", \"songId\" : 1, \"songRating\" : -100}";
        Assert.assertEquals("{\"error\":\"Rating not valid\"}", server.addRating(requestJsonString));
        server.stopServer("saveRatingTest.json");
    }

    @Test
    public void test_deleteRating_equalsUserRating() throws Exception {
        String requestJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\", \"songId\" : 1}";
        Assert.assertEquals("\"{}\"",server.deleteRating(requestJsonString));
        server.stopServer("saveRatingTest.json");
    }
    @Test
    public void test_deleteRating_someoneElses() throws Exception {
        String requestJsonString = "{\"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\", \"songId\" : 2}";//Васёк удаляте петькин рейтинг
        Assert.assertEquals("{\"error\":\"Rating cannot be deleted\"}", server.deleteRating(requestJsonString));
    }

    /**
     * Автор предложения ...... не вправе ни изменить, ни удалить свою оценку.
     */
    @Test
    public void test_deleteRating_its() throws Exception {
        String requestJsonString = "{\"token\" : \"a6acedd8-213f-4018-b61f-d4b1a0a78418\", \"songId\" : 1}";//Васек
        Assert.assertEquals("{\"error\":\"The Creator of the song can't delete the rating\"}", server.deleteRating(requestJsonString));
        server.stopServer("saveRatingTest.json");
    }

    @Test
    public void test_deleteRaiting_incorrectToken() throws Exception {
        String requestJsonString = "{\"token\" : \"fig2020\", \"songId\" : 1}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.deleteRating(requestJsonString));
    }

    @Test
    public void test_deleteRating_incorrectSongId() throws Exception {
        String requestJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\", \"songId\" : -100}";
        Assert.assertEquals("{\"error\":\"Song not found\"}", server.deleteRating(requestJsonString));
    }

    @Test
    public void test_deleteRating_byLoggedOutUser() throws Exception {
        server.logOut("{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\"}");
        String requestJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\", \"songId\" : 1}";
        Assert.assertEquals("{\"error\":\"User not found\"}", server.deleteRating(requestJsonString));
    }

    //todo: сделать комменты, а затем добавить проверку удаления рейтинга
}
