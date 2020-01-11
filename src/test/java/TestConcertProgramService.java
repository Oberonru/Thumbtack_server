import org.junit.Before;
import org.junit.Test;

public class TestConcertProgramService {

    private Server server;

    @Before
    public void serverUpdate() {
        server = new Server();
        server.startServer("concertTest.json");
    }

    @Test
    public void test_getConcertProgram() throws Exception {
        String requestJsonString = "{\"token\" : \"1f00e256-e429-4eec-a24a-4b2901eb1111\"}";
        System.out.println(server.getConcertProgram(requestJsonString));
    }
}
