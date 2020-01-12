package model;

public class Song {
    private String songName;
    private String[] composers;
    private String[] author;
    private String musician;
    private double songDuration;
    private String login;
    private int songId;

    public Song() {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String[] getComposers() {
        return composers;
    }

    public void setComposers(String[] composers) {
        this.composers = composers;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public String getMusician() {
        return musician;
    }

    public void setMusician(String musician) {
        this.musician = musician;
    }

    public double getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(double songDuration) {
        this.songDuration = songDuration;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getSongId() {
        return songId;
    }
}
