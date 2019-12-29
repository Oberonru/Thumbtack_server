import com.fasterxml.jackson.databind.ObjectMapper;
import dao.RaitingDaoImpl;
import dao.SongDaoImpl;
import database.DataBase;
import model.Song;
import request.RatingDtoRequest;
import request.RegisterSongDtoRequest;

public class SongService {

    private SongDaoImpl songData = new SongDaoImpl();
    private ObjectMapper mapper = new ObjectMapper();
    private UserService userService = new UserService();

    public SongService() {
    }

    public Song createSong(String songName, String[] composer, String[] author, String musician, double songDuration,
                           String token, int songId, int rate) {
        Song song = new Song();
        song.setSongName(songName);
        song.setComposer(composer);
        song.setAuthor(author);
        song.setMusician(musician);
        song.setSongDuration(songDuration);
        song.setToken(token);
        song.setSongId(1);
        song.setSongRaiting(5);
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
        //проверка на залогиненность,далее уже можно в запросе добавить данные песни, добавим токен, идПесни, рейтинг)
        RegisterSongDtoRequest request = mapper.readValue(requestJsonString, RegisterSongDtoRequest.class);
        //todo: в request токен должен быть как logIn, как он будет суюда передаваться?
        if (userService.getUserByToken(request.getToken()) != null) {
            Song newSong = createSong(request.getSongName(), request.getComposer(), request.getAuthor(),
                    request.getMusician(), request.getSongDuration(), request.getToken(), 1, 5);
            //изменяем id песни на новый
            generateSongId(newSong);
            songData.insert(newSong);
            return "{}";
        } else return "{\"error\" : \"User not found\"}";
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

    public String addRaiting(String requestJsonString) throws Exception {
        RaitingDaoImpl raitingDao = new RaitingDaoImpl();
        RatingDtoRequest ratingRequest = mapper.readValue(requestJsonString, RatingDtoRequest.class);
        //проверка на границы рейтинга , от 1 до 5
        if (!verifyRating(ratingRequest.getSongRaiting())) {
            return "{\"error\" : \"raiting is not valid\"}";
        }
        if (userService.getUserByToken(ratingRequest.getToken()) == null) {
            return "{\"error\" : \"User not found\"}";
        }
        //todo: должен быть по идее userSongList список песен пользователя, хотя в задании это не просится...
        //todo: а в этом списке уже искать песни

        Song song = findSongById(ratingRequest.getSongId());
        if (song != null) {
            song.setSongRaiting(ratingRequest.getSongRaiting());
            return "{}";
        }
        return "{\"error\" : \"the operation cannot be performed\"}";
    }

    private boolean verifyRating(int raiting) {
        return raiting > 0 && raiting < 6;
    }

    private Song findSongById(int songId) {
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
        DataBase db = DataBase.getInstance();
        //берется последняя песня и к её songId добавляется единица
        int newId = db.getSongList().get(db.getSongList().size() - 1).getSongId() + 1;
        song.setSongId(newId);
    }

}
