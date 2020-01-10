import com.fasterxml.jackson.databind.ObjectMapper;
import dao.RatingDaoImpl;
import database.DataBase;
import model.Rating;
import model.Song;
import model.User;
import request.DeleteSongRatingDtoRequest;
import request.RatingDtoRequest;

public class RaitingService {
    private UserService userService = new UserService();
    private DataBase db = DataBase.getInstance();
    private RatingDaoImpl ratingDao = new RatingDaoImpl();

    /**
     * Радиослушатель, предложивший песню в состав концерта, считается автором этого предложения.
     * Радиослушатели могут ставить свои оценки предлагаемым в программу песням по шкале 1..5. Радиослушатели вправе
     * изменить свою оценку или вообще удалить ее в любое время. Автор предложения автоматически оценивает свое
     * предложение оценкой “5” и не вправе ни изменить, ни удалить свою оценку.
     * Радиослушатели, сделавшие свое предложение, могут отменить его. Если на момент отмены предложение не получило
     * никаких оценок от других радиослушателей, оно удаляется.
     */

    public String addRaiting(RatingDtoRequest request) throws Exception {

        if (!verifyRating(request.getSongRating())) {
            throw new Exception("Rating not valid");
        }
        User user = userService.getUserByLogin(request.getLogin());
        if (user == null) {
            throw new Exception("User not found");
        }
        Song song = db.findSongById(request.getSongId());
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

        Song song = db.findSongById(request.getSongId());
        if (song == null) {
            throw new Exception("Song not found");
        }

        db.deleteRating(new Rating(user.getLogin(), request.getSongId()));
        return "{}";
    }
}
