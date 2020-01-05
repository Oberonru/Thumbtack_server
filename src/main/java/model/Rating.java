package model;

public class Rating {
    private String login;
    private int songId;
    private int songRating;

    public Rating() {}

    public Rating(String login, int songId, int songRating) {
        this.login = login;
        this.songId = songId;
        this.songRating = songRating;
    }

    public Rating(String login, int songId) {
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
