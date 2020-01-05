package model;

public class Raiting {
    private String login;
    private int songId;
    private int songRating;

    public Raiting() {}

    public Raiting(String login, int songId, int songRating) {
        this.login = login;
        this.songId = songId;
        this.songRating = songRating;
    }

    public Raiting(String login, int songId) {
        this(login, songId, 0);
    }

    public String getLogin() {
        return login;
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
