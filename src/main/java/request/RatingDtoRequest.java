package request;

public class RatingDtoRequest {
    private String token;
    private int songId;
    private int songRaiting;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getSongRaiting() {
        return songRaiting;
    }

    public void setSongRaiting(int songRaiting) {
        this.songRaiting = songRaiting;
    }
}
