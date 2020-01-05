package model;

public class Comment {
    private String login;
    private String content;
    private int songId;

    public Comment() {
    }

    public Comment(String login, String content) {
        this.login = login;
        this.content = content;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
