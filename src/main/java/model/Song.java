package model;

public class Song {
    private String songName;
    private String[] composer;
    private String[] author;
    private String musician;
    private double songDuration;
    private String token;

    private int songId;
    private int songRaiting;

    public Song() {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String[] getComposer() {
        return composer;
    }

    public void setComposer(String[] composer) {
        this.composer = composer;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getSongId() {
        return songId;
    }

    public int getSongRaiting() {
        return songRaiting;
    }

    public void setSongRaiting(int songRaiting) {
        this.songRaiting = songRaiting;
    }
}
