import com.fasterxml.jackson.databind.ObjectMapper;
import dao.RatingDaoImpl;
import dao.SongDaoImpl;
import database.DataBase;
import model.Rating;
import model.Song;
import model.User;
import request.DeleteSongDtoRequest;
import request.FindByComposersDtoRequest;
import request.GetSongsDtoRequest;
import request.RegisterSongDtoRequest;
import response.ErrorDtoResponse;
import response.FindByComposersDtoResponse;
import response.GetSongsDtoResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongService {

    private SongDaoImpl songDao = new SongDaoImpl();
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
        song.setComposers(composer);
        song.setAuthor(author);
        song.setMusician(musician);
        song.setSongDuration(songDuration);
        song.setLogin(userService.getUserByToken(token).getLogin());
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
        ErrorDtoResponse response = new ErrorDtoResponse("User not found");
        User user = userService.getUserByToken(request.getToken());
        if (user != null) {
            Song newSong = createSong(request.getSongName(), request.getComposer(), request.getAuthor(),
                    request.getMusician(), request.getSongDuration(), request.getToken());
            generateSongId(newSong);
            songDao.insert(newSong);
            ratingDao.updateRating(new Rating(user.getLogin(), newSong.getSongId(), 5));
            return "{}";
        } else return mapper.writeValueAsString(response);
    }

    /**
     * Радиослушатели, сделавшие свое предложение, могут отменить его. Если на момент отмены предложение не получило
     * * никаких оценок от других радиослушателей, оно удаляется.
     *
     * @return
     */
    public String deleteSong(String requestJsonString) throws Exception {
        DeleteSongDtoRequest request = mapper.readValue(requestJsonString, DeleteSongDtoRequest.class);
        Song song = db.findSongById(request.getSongId());
        User user = userService.getUserByToken(request.getToken());
        if (user == null) {
            ErrorDtoResponse response = new ErrorDtoResponse("User not found");
            return mapper.writeValueAsString(response);
        }
        if (song == null) {
            ErrorDtoResponse response = new ErrorDtoResponse("Song not found");
            return mapper.writeValueAsString(response);
        }
        return songDao.deleteSong(song);
    }

    public String findSongByComposer(String requestJsonString) throws Exception {
        List<Song> songs = new ArrayList<Song>();
        FindByComposersDtoRequest request = mapper.readValue(requestJsonString, FindByComposersDtoRequest.class);
        User user = userService.getUserByToken(request.getToken());
        if (user == null) {
            return mapper.writeValueAsString(new ErrorDtoResponse("User not found"));
        }
        for (Song song : songDao.getSongList()) {
            if (Arrays.asList(song.getComposers()).containsAll(Arrays.asList(request.getComposers()))) {
                songs.add(song);
            }
        }
//        for (int i = 0; i < songDao.getSongList().get(i).getComposers().length; i++) {
//            for (int j = 0; j < request.getComposers().length; j++) {
//                if (songDao.getSongList().get(i).getComposers()[i].equals(request.getComposers()[j])) {
//                    songs.add(songDao.getSongList().get(i));
//                }
//            }
//        }
        FindByComposersDtoResponse response = new FindByComposersDtoResponse();
        response.getSongs().addAll(songs);
        if (songs.size() == 0) {
            ErrorDtoResponse errorResponse = new ErrorDtoResponse("Composers not found");
            return mapper.writeValueAsString(errorResponse);
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }

    private void generateSongId(Song song) {
        if (db.getSongList().size() != 0) {
            int newId = db.getSongList().get(db.getSongList().size() - 1).getSongId() + 1;
            song.setSongId(newId);
        } else return;
    }

    /**
     * Радиослушатель  получает список песен. requestJsonString содержит параметры для отбора песен и token,
     * полученный как результат выполнения команды регистрации радиослушателя. Метод при успешном выполнении возвращает
     * json с описанием всех песен. Если же команду почему-то выполнить нельзя, возвращает json с элементом “error”
     *
     * @param requestJsonString
     * @return Как узнать, что в параметрах для отбора песен именно автор песни, а не композитор? где его искать?
     */

    public String getSongs(String requestJsonString) throws Exception {
        GetSongsDtoRequest getSongsDtoRequest = mapper.readValue(requestJsonString, GetSongsDtoRequest.class);
        User user = userService.getUserByToken(getSongsDtoRequest.getToken());
        if (user != null) {
            GetSongsDtoResponse getSongsDtoResponse = new GetSongsDtoResponse();
            getSongsDtoResponse.getSongList().addAll(songDao.getSongList());
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getSongsDtoResponse);
        }
        ErrorDtoResponse response = new ErrorDtoResponse("User not found");
        return mapper.writeValueAsString(response);
    }
}
