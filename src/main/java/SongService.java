import com.fasterxml.jackson.databind.ObjectMapper;
import dao.SongDaoImpl;
import model.Song;
import request.RegisterSongDtoRequest;
import response.ErrorDtoResponse;

public class SongService {

    private SongDaoImpl songData = new SongDaoImpl();
    private ObjectMapper mapper = new ObjectMapper();
    private UserService userService = new UserService();
    private SongDaoImpl songDao = new SongDaoImpl();

    public SongService() {
    }

    public Song createSong(String songName, String[] composer, String[] author, String musician, double songDuration,
                           String token) {
        Song song = new Song();
        song.setSongName(songName);
        song.setComposer(composer);
        song.setAuthor(author);
        song.setMusician(musician);
        song.setSongDuration(songDuration);
        song.setToken(token);
        return song;
    }

    //Добавление песни в базу данных
    public void addSongToDataBase(Song song) {
        songData.insert(song);
    }

    /**
     * Радиослушатель добавляет новую песню на сервер. requestJsonString содержит описание песни и token, полученный
     * как результат выполнения команды регистрации радиослушателя. Метод при успешном выполнении возвращает пустой json
     * Если же команду почему-то выполнить нельзя, возвращает json с элементом “error”
     *
     * @param requestJsonString
     * @return
     */
    public String addSong(String requestJsonString) throws Exception {
        RegisterSongDtoRequest request = mapper.readValue(requestJsonString, RegisterSongDtoRequest.class);
        Song newSong = createSong(request.getSongName(), request.getComposer(), request.getAuthor(),
                request.getMusician(), request.getSongDuration(), request.getToken());
        if (!userService.validateToken(request.getToken())) {
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse();
            errorDtoResponse.error = "Token in invalid";
            return mapper.writeValueAsString(errorDtoResponse);
        }
        songData.insert(newSong);
        return "{}";
    }
}
