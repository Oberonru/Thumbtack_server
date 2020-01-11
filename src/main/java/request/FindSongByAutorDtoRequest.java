package request;

public class FindSongByAutorDtoRequest {
    private String token;
    private String[] author;

    public String getToken() {
        return token;
    }

    public String[] getAuthor() {
        return author;
    }
}
