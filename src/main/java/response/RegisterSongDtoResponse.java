package response;

public class RegisterSongDtoResponse {
    private String emptyResponse;

    public String getEmptyResponse() {
        return emptyResponse;
    }

    //todo: так можно? уже заданной строке переустановить значение? ???( вот если б так было private String emptyResponse = "{}";)
    public void setEmptyResponse(String emptyResponse) {
        this.emptyResponse = emptyResponse;
    }

    public String getEmtyResponse() {
        return emptyResponse;
    }
}
