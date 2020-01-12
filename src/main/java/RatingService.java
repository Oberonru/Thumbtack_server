import dao.RatingDaoImpl;
import dao.SongDaoImpl;
import model.Rating;
import model.Song;
import model.User;
import request.DeleteSongRatingDtoRequest;
import request.RatingDtoRequest;

public class RatingService {
    private UserService userService = new UserService();
    private RatingDaoImpl ratingDao = new RatingDaoImpl();
    private SongDaoImpl songDao = new SongDaoImpl();

    public String addRaiting(RatingDtoRequest request) throws Exception {

        if (!verifyRating(request.getSongRating())) {
            throw new Exception("Rating not valid");
        }
        User user = userService.getUserByLogin(request.getLogin());
        if (user == null) {
            throw new Exception("User not found");
        }
        Song song = songDao.findSongById(request.getSongId());
        if (song == null) {
            throw new Exception("Song not found");
        }

        ratingDao.insert(new Rating(user.getLogin(), song.getSongId(), request.getSongRating()));
        return "{}";
    }

    public boolean verifyRating(int raiting) {
        return raiting > 0 && raiting < 6;
    }

    public String deleteRating(DeleteSongRatingDtoRequest request) throws Exception {
        User user = userService.getUserByToken(request.getToken());
        if (user == null) {
            throw new Exception("User not found");
        }

        Song song = songDao.findSongById(request.getSongId());
        if (song == null) {
            throw new Exception("Song not found");
        }

        ratingDao.deleteRating(new Rating(user.getLogin(), request.getSongId()));
        return "{}";
    }
}
