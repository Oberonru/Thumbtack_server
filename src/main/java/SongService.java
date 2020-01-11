import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CommentDaoImpl;
import dao.RatingDaoImpl;
import dao.SongDaoImpl;
import database.DataBase;
import model.Comment;
import model.Rating;
import model.Song;
import model.User;
import request.*;
import response.FindSongByAutorDtoResponse;
import response.FindSongByComposersDtoResponse;
import response.GetSongsDtoResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongService {

    private SongDaoImpl songDao = new SongDaoImpl();
    private RatingDaoImpl ratingDao = new RatingDaoImpl();
    private CommentDaoImpl commentDao = new CommentDaoImpl();
    private ObjectMapper mapper = new ObjectMapper();
    private UserService userService = new UserService();

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

        if (findSongBySongName(request.getSongName())) {
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
     */
    public String deleteSong(DeleteSongDtoRequest request) throws Exception {
        Song song = songDao.findSongById(request.getSongId());
        User user = userService.getUserByToken(request.getToken());

        if (user == null) {
            throw new Exception("User not found");
        }
        if (song == null) {
            throw new Exception("Song not found");
        }

        List<Comment> songComments = commentDao.getCommentsBySongId(song.getSongId());

            if (user.getLogin().equals(song.getLogin())) {
                if (ratingDao.getRatingsCount(song.getSongId()) == 1 && songComments.size() == 0) {
                    songDao.getSongList().remove(song);
                    return "{}";
                }

                else {
                    for (Rating rating : ratingDao.getRatingList()) {
                        if (rating.getLogin().equals(song.getLogin())) {
                            //я прссто удаляю рейтинг, можно засетить автором рейтинга login : progerCommunity, не понятно условие
                            ratingDao.getRatingList().remove(rating);
                            throw new Exception("Song rating is deleted, the user can't delete song");
                        }
                    }
                }
            }

        throw new Exception("The user can't delete song");
    }

    public String getSongs(GetSongsDtoRequest request) throws Exception {

        User user = userService.getUserByToken(request.getToken());
        if (user == null) {
            throw new Exception("User not found");
        }
        GetSongsDtoResponse getSongsDtoResponse = new GetSongsDtoResponse();
        getSongsDtoResponse.getSongList().addAll(songDao.getSongList());

        return mapper.writeValueAsString(getSongsDtoResponse);
    }

    public String findSongByComposer(FindSongByComposersDtoRequest request) throws Exception {
        List<Song> songs = new ArrayList<Song>();

        User user = userService.getUserByToken(request.getToken());
        if (user == null) {
            throw new Exception("User not found");
        }

        for (Song song : songDao.getSongList()) {
            if (Arrays.asList(song.getComposers()).containsAll(Arrays.asList(request.getComposers()))) {
                songs.add(song);
            }
        }

        FindSongByComposersDtoResponse response = new FindSongByComposersDtoResponse();
        response.getSongList().addAll(songs);
        if (songs.size() == 0) {
           throw new Exception("Composers not found");
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }

    public String findSongByAuthor(FindSongByAutorDtoRequest request) throws Exception {
        List<Song> songs = new ArrayList<Song>();

        User user = userService.getUserByToken(request.getToken());
        if (user == null) {
            throw new Exception("User not found");
        }

        for (Song song : songDao.getSongList()) {
            if (Arrays.asList(song.getAuthor()).containsAll(Arrays.asList(request.getAuthor()))) {
                songs.add(song);
            }
        }

        if (songs.size() == 0) {
            throw new Exception("Authors is not found");
        }

        FindSongByAutorDtoResponse response = new FindSongByAutorDtoResponse();
        response.getSongList().addAll(songs);
        return mapper.writeValueAsString(response);
    }

    private boolean findSongBySongName(String songName) {
        for (Song song : songDao.getSongList()) {
            if (song.getSongName().equalsIgnoreCase(songName)) {
                return true;
            }
        }
        return false;
    }

    private void generateSongId(Song song) {
        if (songDao.getSongList().size() != 0) {
            int newId = songDao.getSongList().get(songDao.getSongList().size() - 1).getSongId() + 1;
            song.setSongId(newId);
        }
    }


}
