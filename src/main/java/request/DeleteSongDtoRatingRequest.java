package request;

public class DeleteSongDtoRatingRequest {
    private String token;
    private int songId;

    public String getToken() {
        return token;
    }

    public int getSongId() {
        return songId;
    }
}
