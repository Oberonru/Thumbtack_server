import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestCommentService {
    private Server server;

    @Before
    public void setupServer() {
        server = new Server();
        server.startServer("commentTest.json");
    }

    @Test
    public void test_addComment() throws Exception {
        String requestLoginJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\"," +
                " \"content\" : \"Фуфло, а не песня\", \"songId\" : 3}";
        Assert.assertEquals("\"{}\"", server.addComment(requestLoginJsonString));
    }

    @Test
    public void test_addComment_token_notValid() throws Exception {
        String requestLoginJsonString = "{\"token\" : \"43434\"," +
                " \"content\" : \"Фуфло, а не песня\", \"songId\" : 3}";
        Assert.assertEquals("{\"error\":\"Comment is not added\"}", server.addComment(requestLoginJsonString));
    }

    @Test
    public void test_addComment_songId_notValid() throws Exception {
        String requestLoginJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\"," +
                " \"content\" : \"Фуфло, а не песня\", \"songId\" : -1}";
        Assert.assertEquals("{\"error\":\"Comment is not added\"}", server.addComment(requestLoginJsonString));
    }

    @Test
    public void test_addComment_contentTooLong() throws Exception {
        String requestLoginJsonString = "{\"token\" : \"1f07e256-e429-4eec-a24a-4b2901eb7cf6\"," +
                " \"content\" : \"Бла, бла бла куууууууууууууууууууукааааааааааааааааааааааааааааааааааа\", \"songId\" :3}";
        Assert.assertEquals("{\"error\":\"content is too Long\"}", server.addComment(requestLoginJsonString));
    }

    @Test
    public void test_addComment_addReplyComment() throws Exception {
        String requestLoginJsonString = "{\"token\" : \"9f0e256-e429-4eec-a24a-4b2901eb00000\"," +
                " \"content\" : \"Да фуфлище полное!!!\", \"songId\" : 3, \"replyCommentId\" : 1}";
        Assert.assertEquals("\"{}\"", server.addComment(requestLoginJsonString));
        server.stopServer("saveCommentTest.json");
    }


}
