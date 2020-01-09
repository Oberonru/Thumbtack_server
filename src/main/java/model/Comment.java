package model;

public class Comment {
    private int id;
    private String login;
    private String content;
    private int songId;
    private int replyCommentId;

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

    public int getId() {
        return id;
    }

    public int getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(int replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
