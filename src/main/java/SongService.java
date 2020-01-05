import com.fasterxml.jackson.databind.ObjectMapper;
import dao.RatingDaoImpl;
import dao.SongDaoImpl;
import database.DataBase;
import model.Rating;
import model.Song;
import model.User;
import request.DeleteSongDtoRequest;
import request.RegisterSongDtoRequest;

public class SongService {

    private SongDaoImpl songData = new SongDaoImpl();
    private RatingDaoImpl ratingDao = new RatingDaoImpl();
    private ObjectMapper mapper = new ObjectMapper();
    private UserService userService = new UserService();
    private DataBase db = DataBase.getInstance();


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
        song.setLogin(db.getUserByToken(token).getLogin());
        song.setSongId(1);
        return song;
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
        User user = db.getUserByToken(request.getToken());
        if (user != null) {
            Song newSong = createSong(request.getSongName(), request.getComposer(), request.getAuthor(),
                    request.getMusician(), request.getSongDuration(), request.getToken());
            generateSongId(newSong);
            songData.insert(newSong);
            ratingDao.updateRating(new Rating(user.getLogin(), newSong.getSongId(), 5));
            return "{}";
        } else return "{\"error\" : \"User not found\"}";
    }

    /**
     * Радиослушатели, сделавшие свое предложение, могут отменить его. Если на момент отмены предложение не получило
     * * никаких оценок от других радиослушателей, оно удаляется.
     *
     * @return
     */
    public String deleteSong(String requestJsonString) throws Exception {
        DeleteSongDtoRequest deleteSongDtoRequest = mapper.readValue(requestJsonString, DeleteSongDtoRequest.class);
        System.out.println(db.frequencyRaitings(deleteSongDtoRequest.getSongId()));
        Song song = db.findSongById(deleteSongDtoRequest.getSongId());
        if (song == null) {
            return "{\"error\" : \"Song not found\"}";
        }
        return db.deleteSong(song);

    }

    //прежде чем добавить оценку песне, нужно её сначала найти
    public Song findSongByAuthor(String author) {
        DataBase db = DataBase.getInstance();
        for (Song song : db.getSongList()) {
            if (song.getAuthor().equals(author)) {
                return song;
            }
        }
        return null;
    }

    private void generateSongId(Song song) {
        //берется последняя песня и к её songId добавляется единица
        if (db.getSongList().size() != 0) {
            int newId = db.getSongList().get(db.getSongList().size() - 1).getSongId() + 1;
            song.setSongId(newId);
        }
        else return;
    }

    /**
     * Радиослушатель  получает список песен. requestJsonString содержит параметры для отбора песен и token,
     * полученный как результат выполнения команды регистрации радиослушателя. Метод при успешном выполнении возвращает
     * json с описанием всех песен. Если же команду почему-то выполнить нельзя, возвращает json с элементом “error”
     * @param requestJsonString
     * @return
     *
     * Как узнать, что в параметрах для отбора песен именно автор песни, а не композитор? где его искать?
     */

    public String getSongs(String requestJsonString) {
        return "fig";
    }}
