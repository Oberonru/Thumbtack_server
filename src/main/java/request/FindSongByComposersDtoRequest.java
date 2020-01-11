package request;

public class FindSongByComposersDtoRequest {
    private String token;
    private String[] composers;

    public FindSongByComposersDtoRequest() {}

    public String getToken() {
        return token;
    }

    public String[] getComposers() {
        return composers;
    }
}
