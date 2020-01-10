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
     */
    public String addSong(RegisterSongDtoRequest request) throws Exception {
        User user = userService.getUserByToken(request.getToken());

        if (user == null) {
            throw new Exception("User not found");
        }

        if (findtSongBySongName(request.getSongName())) {
            throw new Exception("Such a song already exsists");
        }

        Song newSong = createSong(request.getSongName(), request.getComposer(), request.getAuthor(),
                request.getMusician(), request.getSongDuration(), request.getToken());
        generateSongId(newSong);
        songDao.insert(newSong);
        ratingDao.insert(new Rating(user.getLogin(), newSong.getSongId(), 5));

        return "{}";
    }

    /**
     * Радиослушатели, сделавшие свое предложение, могут отменить его. Если на момент отмены предложение не получило
     * * никаких оценок от других радиослушателей, оно удаляется.
     *
     * @return
     */
    public String deleteSong(DeleteSongDtoRequest request) throws Exception {

        Song song = db.findSongById(request.getSongId());
        User user = userService.getUserByToken(request.getToken());
        if (user == null) {
            throw new Exception("User not found");
        }
        if (song == null) {
            throw new Exception("Song not found");
        }
        return songDao.deleteSong(song);
    }

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

        FindByComposersDtoResponse response = new FindByComposersDtoResponse();
        response.getSongs().addAll(songs);
        if (songs.size() == 0) {
            ErrorDtoResponse errorResponse = new ErrorDtoResponse("Composers not found");
            return mapper.writeValueAsString(errorResponse);
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }

    private boolean findtSongBySongName(String songName) {
        for (Song song : songDao.getSongList()) {
            if (song.getSongName().equalsIgnoreCase(songName)) {
                return true;
            }
        }
        return false;
    }

    private void generateSongId(Song song) {
        if (db.getSongList().size() != 0) {
            int newId = db.getSongList().get(db.getSongList().size() - 1).getSongId() + 1;
            song.setSongId(newId);
        } else return;
    }


}
