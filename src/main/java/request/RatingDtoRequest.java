package request;

public class RatingDtoRequest {
    private String login;
    private int songId;
    private int songRating;

    public String getLogin() {
        return login;
    }

    public void setLogin(String token) {
        this.login = token;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getSongRating() {
        return songRating;
    }

    public void setSongRating(int songRating) {
        this.songRating = songRating;
    }
}
