package request;

public class CommentDtoRequest {
    private String token;
    private String content;
    private int songId;

    public CommentDtoRequest() {}

    public String getToken() {
        return token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSongId() {
        return songId;
    }
}
