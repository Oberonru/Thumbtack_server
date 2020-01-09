package request;

public class FindByComposersDtoRequest {
    private String token;
    private String[] composers;

    public FindByComposersDtoRequest() {}

    public String getToken() {
        return token;
    }

    public String[] getComposers() {
        return composers;
    }
}
