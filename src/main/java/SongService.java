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

    public void addSongToDataBase(Song song, String saveDataFileName) {
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
        if (userService.getUserByToken(request.getToken()) == null) {
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse();
            errorDtoResponse.error = "Token in invalid";
            return mapper.writeValueAsString(errorDtoResponse);
        }
        Song newSong = createSong(request.getSongName(), request.getComposer(), request.getAuthor(),
                request.getMusician(), request.getSongDuration(), request.getToken());

        songData.insert(newSong);
        return "{}";
    }

    //TODO:чтобы добавить рейтинг песне нужно, проверить на валидность токен, затем найти песню и добавить рейтинг...
    /**
     * Радиослушатель, предложивший песню в состав концерта, считается автором этого предложения.
     * Радиослушатели могут ставить свои оценки предлагаемым в программу песням по шкале 1..5. Радиослушатели вправе
     * изменить свою оценку или вообще удалить ее в любое время. Автор предложения автоматически оценивает свое
     * предложение оценкой “5” и не вправе ни изменить, ни удалить свою оценку.
     * Радиослушатели, сделавшие свое предложение, могут отменить его. Если на момент отмены предложение не получило
     * никаких оценок от других радиослушателей, оно удаляется.
     */

    public void addRate(String requestJsonString)throws Exception {
        //по идее в запросе мне нужн токен, зачем создавать какой то другой однотипный DTO объек, когда есть LogOutRequest?
        //LogOutDtoRequest request = mapper.readValue(requestJsonString, LogOutDtoRequest.class);
        //token -> userId,                      songId, rate

    }

}
