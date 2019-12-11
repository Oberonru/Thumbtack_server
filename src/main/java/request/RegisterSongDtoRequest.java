package request;

public class RegisterSongDtoRequest {
    private String songName;
    private String[] composer;
    private String[] author;
    private String musician;
    private int songDuration;
    private String token;

    public RegisterSongDtoRequest(String songName, String[] composer, String[] author, String musician,
                                  int songDuration, String token) {
        this.songName = songName;
        this.composer = composer;
        this.author = author;
        this.musician = musician;
        this.songDuration = songDuration;
        this.token = token;
    }

    public RegisterSongDtoRequest() {}

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

    public int getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(int songDuration) {
        this.songDuration = songDuration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
