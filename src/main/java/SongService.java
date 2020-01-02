import com.fasterxml.jackson.databind.ObjectMapper;
import dao.RaitingDaoImpl;
import dao.SongDaoImpl;
import database.DataBase;
import model.Raiting;
import model.Song;
import model.User;
import request.DeleteSongRaitingRequest;
import request.RatingDtoRequest;
import request.RegisterSongDtoRequest;

public class SongService {

    private SongDaoImpl songData = new SongDaoImpl();
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
        song.setToken(token);
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
        User user = userService.getUserByToken(request.getToken());
        if (user != null) {
            Song newSong = createSong(request.getSongName(), request.getComposer(), request.getAuthor(),
                    request.getMusician(), request.getSongDuration(), request.getToken());
            generateSongId(newSong);
            songData.insert(newSong);
            db.updateRaiting(new Raiting(user.getLogin(), newSong.getSongId(), 5));
            return "{}";
        } else return "{\"error\" : \"User not found\"}";
    }

    /**
     * Радиослушатель, предложивший песню в состав концерта, считается автором этого предложения.
     * Радиослушатели могут ставить свои оценки предлагаемым в программу песням по шкале 1..5. Радиослушатели вправе
     * изменить свою оценку или вообще удалить ее в любое время. Автор предложения автоматически оценивает свое
     * предложение оценкой “5” и не вправе ни изменить, ни удалить свою оценку.
     * Радиослушатели, сделавшие свое предложение, могут отменить его. Если на момент отмены предложение не получило
     * никаких оценок от других радиослушателей, оно удаляется.
     */

    public String addRaiting(String requestJsonString) throws Exception {
        RatingDtoRequest ratingRequest = mapper.readValue(requestJsonString, RatingDtoRequest.class);
        if (!verifyRating(ratingRequest.getSongRaiting())) {
            return "{\"error\" : \"raiting is not valid\"}";
        }
        User user = userService.getUserByLogin(ratingRequest.getLogin());
        if (user == null) {
            return "{\"error\" : \"User not found\"}";
        }
        Song song = findSongById(ratingRequest.getSongId());
        if (song != null) {
            db.updateRaiting(new Raiting(user.getLogin(), song.getSongId(), ratingRequest.getSongRaiting()));
            return "{}";
        }
        return "{\"error\" : \"the operation cannot be performed\"}";
    }

    /**
     * Радиослушатели, сделавшие свое предложение, могут отменить его. Если на момент отмены предложение не получило
     * * никаких оценок от других радиослушателей, оно удаляется.
     *
     * @return
     */
    public String deleteSong() {
        return "";
    }


    public boolean verifyRating(int raiting) {
        return raiting > 0 && raiting < 6;
    }

    public Song findSongById(int songId) {
        DataBase db = DataBase.getInstance();
        for (Song song : db.getSongList()) {
            if (song.getSongId() == songId) {
                return song;
            }
        }
        return null;
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
}
